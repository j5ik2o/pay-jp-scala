package com.github.j5ik2o.payjp.scala

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.{ BasicHttpCredentials, RawHeader }
import akka.http.scaladsl.model.{ HttpRequest, HttpResponse }
import akka.stream._
import akka.stream.scaladsl.{ Flow, GraphDSL, Keep, Sink, Source, Unzip, Zip }
import io.circe.parser.parse
import io.circe.{ Decoder, Json }
import monix.eval.Task
import org.slf4j.{ Logger, LoggerFactory }

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ Future, Promise }
import scala.util.Try

class HttpRequestSender(config: ApiConfig)(implicit system: ActorSystem) {

  implicit val materializer = ActorMaterializer()

  private val logger: Logger = LoggerFactory.getLogger(getClass)

  private val poolClientFlow =
    Http().cachedHostConnectionPoolHttps[Int](config.host, config.port)

  private val timeout: FiniteDuration = config.timeoutForToStrict

  private def toJson(jsonString: String): Task[Json] = Task.deferFuture {
    parse(jsonString) match {
      case Right(json) =>
        logger.debug("json = {}", json.spaces2)
        if (json.hcursor.keys.exists(_.exists(_ == "error"))) {
          val acursor = json.hcursor.downField("error")
          acursor
            .get[String]("type")
            .flatMap {
              case "auth_error" =>
                for {
                  status  <- acursor.get[Int]("status")
                  message <- acursor.get[String]("message")
                } yield AuthException(status, message)
              case "client_error" =>
                for {
                  status  <- acursor.get[Int]("status")
                  message <- acursor.get[String]("message")
                  param   <- acursor.get[Option[String]]("param")
                  code    <- acursor.get[Option[String]]("code")
                } yield ClientException(status, message, param, code)
              case "server_error" =>
                for {
                  status  <- acursor.get[Int]("status")
                  message <- acursor.get[String]("message")
                  param   <- acursor.get[Option[String]]("param")
                  code    <- acursor.get[Option[String]]("code")
                } yield ServerException(status, message, param, code)
              case "card_error" =>
                for {
                  status  <- acursor.get[Int]("status")
                  message <- acursor.get[String]("message")
                  code    <- acursor.get[String]("code")
                } yield CardException(status, message, code)
              case _ =>
                Right(new Exception(json.toString))
            }
            .fold({ fs =>
              Future.failed(JsonDecodingException(fs.message))
            }, { v =>
              Future.failed(v)
            })
        } else
          Future.successful(json)
      case Left(error) => Future.failed(JsonParsingException(error.message))
    }
  }

  private def toModel[A](json: Json)(implicit d: Decoder[A]): Task[A] = Task.deferFuture {
    json.as[A] match {
      case Right(r) => Future.successful(r)
      case Left(error) =>
        Future.failed(JsonDecodingException(error.message + ":" + error.history))
    }
  }

  private def responseToModel[A](responseFuture: Task[HttpResponse])(implicit d: Decoder[A]): Task[A] = {
    for {
      httpResponse <- responseFuture
      httpEntity   <- Task.deferFuture(httpResponse.entity.toStrict(timeout))
      json         <- toJson(httpEntity.data.utf8String)
      model        <- toModel(json)
    } yield model
  }

  case class PromiseWithHttpRequest(promise: Promise[HttpResponse], request: HttpRequest)

  private def sendRequestFlow
    : Flow[(HttpRequest, PromiseWithHttpRequest), (Try[HttpResponse], PromiseWithHttpRequest), NotUsed] =
    Flow.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._
      val unzip = b.add(Unzip[HttpRequest, PromiseWithHttpRequest])
      val zip   = b.add(Zip[Try[HttpResponse], PromiseWithHttpRequest])
      unzip.out0 ~> Flow[HttpRequest].map((_, 1)) ~> poolClientFlow.map(_._1) ~> zip.in0
      unzip.out1 ~> zip.in1
      FlowShape(unzip.in, zip.out)
    })

  private val requestQueue = Source
    .queue[PromiseWithHttpRequest](config.requestBufferSize, OverflowStrategy.dropNew)
    .map {
      case p @ PromiseWithHttpRequest(_, request) => (request, p)
    }
    .via(sendRequestFlow)
    .map {
      case (triedResponse, PromiseWithHttpRequest(promise, _)) =>
        promise.complete(triedResponse)
    }
    .toMat(Sink.ignore)(Keep.left)
    .run()

  def sendRequest[A: Decoder](request: HttpRequest,
                              secretKey: String,
                              headers: List[RawHeader] = List.empty): Task[A] = {
    val responseTask = Task.deferFutureAction { implicit ec =>
      val promise = Promise[HttpResponse]()
      requestQueue
        .offer(
          PromiseWithHttpRequest(promise,
                                 request.withHeaders(headers).addCredentials(BasicHttpCredentials(secretKey, "")))
        )
        .flatMap {
          case QueueOfferResult.Enqueued =>
            promise.future
          case QueueOfferResult.Failure(t) =>
            Future.failed(new Exception("Failed to send request", t))
          case QueueOfferResult.Dropped =>
            Future.failed(
              new Exception(
                s"Failed to send request, the queue buffer was full."
              )
            )
          case QueueOfferResult.QueueClosed =>
            Future.failed(new Exception("Failed to send request, the queue was closed"))
        }
    }
    responseToModel[A](responseTask)
  }
}
