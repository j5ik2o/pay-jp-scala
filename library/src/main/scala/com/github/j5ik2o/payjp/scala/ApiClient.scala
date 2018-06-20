package com.github.j5ik2o.payjp.scala

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Sink, Source }
import com.github.j5ik2o.payjp.scala.model._
import io.circe.parser._
import io.circe.syntax._
import io.circe.{ Decoder, Json }
import monix.eval.Task

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

case class JsonParsingException(message: String)  extends Exception(message)
case class JsonDecodingException(message: String) extends Exception(message)

object ApiClient {

  implicit class ToMapOps[A](val a: A) extends AnyVal {

    import shapeless._
    import ops.record._

    def toMap[L <: HList](implicit
                          gen: LabelledGeneric.Aux[A, L],
                          tmr: ToMap[L]): Map[String, String] = {
      val m: Map[tmr.Key, tmr.Value] = tmr(gen.to(a))
      m.filterNot {
          case (_: Symbol, v: Option[_]) => v == None
          case _                         => false
        }
        .map {
          case (k: Symbol, v: List[_])   => k.name -> v.mkString(",")
          case (k: Symbol, v: Int)       => k.name -> v.toString
          case (k: Symbol, v: String)    => k.name -> v
          case (k: Symbol, v: Option[_]) => k.name -> v.map(_.toString).orNull[String]
          case _                         => throw new AssertionError
        }
    }
  }
}

class ApiClient(config: ApiConfig)(implicit system: ActorSystem) {
  import ApiClient._
  private implicit val materializer = ActorMaterializer()

  private val poolClientFlow =
    Http().cachedHostConnectionPoolHttps[Int](config.host, config.port)

  private val timeout: FiniteDuration = config.timeoutForToStrict

  private def toJson(jsonString: String): Task[Json] = Task.deferFuture {
    parse(jsonString) match {
      case Right(json) =>
        println("json = " + json.spaces2)
        if (json.hcursor.keys.exists(_.exists(_ == "error"))) {
          val acursor = json.hcursor.downField("error")
          acursor.downField("type").as[String] match {
            case Right("auth_error") =>
              Future.failed(AuthException(acursor.as[AuthErrorResponse].right.get))
            case Right("client_error") =>
              Future.failed(ClientException(acursor.as[ClientErrorResponse].right.get))
            case _ =>
              Future.failed(new Exception(json.toString()))
          }
        } else
          Future.successful(json)
      case Left(error) => Future.failed(JsonParsingException(error.message))
    }
  }

  private def toModel[A](json: Json)(implicit d: Decoder[A]): Task[A] = Task.deferFuture {
    json.as[A] match {
      case Right(r)    => Future.successful(r)
      case Left(error) => Future.failed(JsonDecodingException(error.message))
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

  def createCustomer(createCustomerRequest: CreateCustomerRequest): Task[CustomerResponse] = {
    val method = HttpMethods.POST
    val path   = s"/v1/customers"
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
      .withEntity(FormData(createCustomerRequest.toMap).toEntity)
    val responseFuture = Source
      .single(request -> 1)
      .via(poolClientFlow)
      .runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[CustomerResponse](Task.fromTry(triedResponse))
    }
  }

  def getCustomer(customerId: String): Task[CustomerResponse] = {
    val method = HttpMethods.GET
    val path   = s"/v1/customers/$customerId"
    val responseFuture = Source
      .single(HttpRequest(uri = path, method = method).addCredentials(BasicHttpCredentials(config.secretKey, "")) -> 1)
      .via(poolClientFlow)
      .runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        println(triedResponse)
        responseToModel[CustomerResponse](Task.fromTry(triedResponse))
    }
  }

  def createCustomerCard(customerId: String)(cardToken: CardToken) = {
    val method = HttpMethods.POST
    val path   = s"/v1/customers/$customerId/cards"
    val responseFuture = Source
      .single(HttpRequest(uri = path, method = method).addCredentials(BasicHttpCredentials(config.secretKey, "")) -> 1)
      .via(poolClientFlow)
      .runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        println(triedResponse)
        responseToModel[CustomerResponse](Task.fromTry(triedResponse))
    }

  }
}
