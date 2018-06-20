package com.github.j5ik2o.payjp.scala

import java.time.ZonedDateTime

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Sink, Source }
import com.github.j5ik2o.payjp.scala.model._
import io.circe.parser._
import io.circe.{ Decoder, Json }
import monix.eval.Task

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

case class JsonParsingException(message: String) extends Exception(message)

case class JsonDecodingException(message: String) extends Exception(message)

object ApiClient {

  def apply(config: ApiConfig)(implicit system: ActorSystem): ApiClient = new ApiClientImpl(config)

}

trait ApiClient {
  // --- 顧客
  def createCustomer(customerIdOpt: Option[CustomerId] = None,
                     emailOpt: Option[String] = None,
                     descriptionOpt: Option[String] = None,
                     cardTokenOpt: Option[CardToken] = None,
                     metaData: Map[String, Any] = Map.empty): Task[Customer]
  def getCustomer(customerId: CustomerId): Task[Customer]
  def updateCustomer(customerId: CustomerId,
                     emailOpt: Option[String] = None,
                     descriptionOpt: Option[String] = None,
                     defaultCardOpt: Option[String] = None,
                     cardOpt: Option[CardToken] = None,
                     metaData: Map[String, String] = Map.empty): Task[Customer]
  def deleteCustomer(customerId: CustomerId): Task[Deleted[CustomerId]]
  def getCustomers(limit: Option[Int] = None,
                   offset: Option[Int] = None,
                   since: Option[ZonedDateTime] = None,
                   until: Option[ZonedDateTime] = None): Task[Collection[Customer]]

  def getCustomerSubscription(customerId: CustomerId, subscriptionId: SubscriptionId): Task[Subscription]
  def getCustomerSubscriptions(customerId: CustomerId,
                               planId: Option[PlanId] = None,
                               status: Option[SubscriptionStatus] = None,
                               limit: Option[Int] = None,
                               offset: Option[Int] = None,
                               since: Option[ZonedDateTime] = None,
                               until: Option[ZonedDateTime] = None): Task[Collection[Subscription]]
  def createPlan(amount: Amount,
                 currency: Currency,
                 interval: PlanInterval,
                 idOpt: Option[String] = None,
                 nameOpt: Option[String] = None,
                 trialDaysOpt: Option[Int] = None,
                 billingDayOpt: Option[Int] = None,
                 metaData: Map[String, String] = Map.empty): Task[Plan]

  def getPlan(planId: PlanId): Task[Plan]
  def updatePlan(planId: PlanId, nameOpt: Option[String] = None, metaData: Map[String, String] = Map.empty): Task[Plan]
  def deletePlan(planId: PlanId): Task[Deleted[String]]
  def getPlans(limit: Option[Int] = None,
               offset: Option[Int] = None,
               since: Option[ZonedDateTime] = None,
               until: Option[ZonedDateTime] = None): Task[Collection[Plan]]
  def createSubscription(customerId: String,
                         planId: PlanId,
                         trialEnd: Option[ZonedDateTime] = None,
                         prorate: Option[Boolean] = None,
                         metaData: Map[String, String] = Map.empty): Task[Subscription]
  def updateSubscription(subscriptionId: SubscriptionId,
                         planId: Option[PlanId] = None,
                         trialEnd: Option[ZonedDateTime] = None,
                         prorate: Option[Boolean] = None,
                         metaData: Map[String, String] = Map.empty): Task[Subscription]
  def pauseSubscription(subscriptionId: SubscriptionId): Task[Subscription]
  def resumeSubscription(subscriptionId: SubscriptionId): Task[Subscription]
  def cancelSubscription(subscriptionId: SubscriptionId): Task[Subscription]
  def deleteSubscription(subscriptionId: SubscriptionId): Task[Deleted[String]]
  def getSubscriptions(planId: Option[PlanId] = None,
                       status: Option[SubscriptionStatus] = None,
                       limit: Option[Int] = None,
                       offset: Option[Int] = None,
                       since: Option[ZonedDateTime] = None,
                       until: Option[ZonedDateTime] = None): Task[Collection[Subscription]]

  def createCharge(amountAndCurrency: Option[(Amount, Currency)],
                   productId: Option[String],
                   customerId: Option[CustomerId],
                   cardToken: Option[CardToken],
                   description: Option[String] = None,
                   capture: Option[Boolean] = None,
                   expiryDays: Option[ZonedDateTime] = None,
                   metaData: Map[String, String] = Map.empty,
                   platformFee: Option[BigDecimal] = None): Task[Charge]
  def getCharge(chargeId: ChargeId): Task[Charge]
  def updateCharge(chargeId: ChargeId,
                   description: Option[String] = None,
                   metaData: Map[String, String] = Map.empty): Task[Charge]
  def refundCharge(chargeId: ChargeId, amount: Option[Amount] = None, refundReason: Option[String] = None): Task[Charge]
  def reauthCharge(chargeId: ChargeId, expiryDays: Option[Int] = None): Task[Charge]
  def captureCharge(chargeId: ChargeId, amount: Option[Amount] = None): Task[Charge]
  def getCharges(customerId: Option[CustomerId] = None,
                 subscriptionId: Option[String] = None,
                 limit: Option[Int] = None,
                 offset: Option[Int] = None,
                 since: Option[ZonedDateTime] = None,
                 until: Option[ZonedDateTime] = None): Task[Collection[Subscription]]
}

class ApiClientImpl(config: ApiConfig)(implicit system: ActorSystem) extends ApiClient {

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
              Future.failed(AuthException(acursor.as[AuthError].right.get))
            case Right("client_error") =>
              Future.failed(ClientException(acursor.as[ClientError].right.get))
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

  private def getParamMap(limit: Option[Int],
                          offset: Option[Int],
                          since: Option[ZonedDateTime],
                          until: Option[ZonedDateTime]): Map[String, String] = {
    val params = limit.map(v => Map("limit" -> v.toString)).getOrElse(Map.empty) ++
    offset.map(v => Map("offset" -> v.toString)).getOrElse(Map.empty) ++
    since.map(v => Map("since"   -> v.toEpochSecond.toString)).getOrElse(Map.empty) ++
    until.map(v => Map("until"   -> v.toEpochSecond.toString)).getOrElse(Map.empty)
    params
  }

  // --- 顧客

  override def createCustomer(customerIdOpt: Option[CustomerId] = None,
                              emailOpt: Option[String] = None,
                              descriptionOpt: Option[String] = None,
                              cardTokenOpt: Option[CardToken] = None,
                              metaData: Map[String, Any] = Map.empty): Task[Customer] = {
    val method = HttpMethods.POST
    val path   = s"/v1/customers"
    val params = customerIdOpt.map(v => Map("id" -> v.value)).getOrElse(Map.empty) ++
    emailOpt.map(v => Map("email"             -> v)).getOrElse(Map.empty) ++
    descriptionOpt.map(v => Map("description" -> v)).getOrElse(Map.empty) ++
    cardTokenOpt.map(v => Map("card"          -> v.value)).getOrElse(Map.empty) ++
    metaData
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
      .withEntity(FormData(params).toEntity)
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Customer](Task.fromTry(triedResponse))
    }
  }

  override def getCustomer(customerId: CustomerId): Task[Customer] = {
    val method         = HttpMethods.GET
    val path           = s"/v1/customers/${customerId.value}"
    val request        = HttpRequest(uri = path, method = method).addCredentials(BasicHttpCredentials(config.secretKey, ""))
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        println(triedResponse)
        responseToModel[Customer](Task.fromTry(triedResponse))
    }
  }

  override def updateCustomer(customerId: CustomerId,
                              emailOpt: Option[String] = None,
                              descriptionOpt: Option[String] = None,
                              defaultCardOpt: Option[String] = None,
                              cardOpt: Option[CardToken] = None,
                              metaData: Map[String, String] = Map.empty): Task[Customer] = {
    val method = HttpMethods.POST
    val path   = s"/v1/customers/${customerId.value}"
    val params = emailOpt.map(v => Map("email" -> v)).getOrElse(Map.empty) ++
    descriptionOpt.map(v => Map("description"  -> v)).getOrElse(Map.empty) ++
    defaultCardOpt.map(v => Map("default_card" -> v)).getOrElse(Map.empty) ++
    cardOpt.map(v => Map("card"                -> v.value)).getOrElse(Map.empty) ++
    metaData
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
      .withEntity(FormData(params).toEntity)
    val responseFuture = Source
      .single(request -> 1)
      .via(poolClientFlow)
      .runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Customer](Task.fromTry(triedResponse))
    }
  }

  override def deleteCustomer(customerId: CustomerId): Task[Deleted[CustomerId]] = {
    val method = HttpMethods.DELETE
    val path   = s"/v1/customers/${customerId.value}"
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
    val responseFuture = Source
      .single(request -> 1)
      .via(poolClientFlow)
      .runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Deleted[CustomerId]](Task.fromTry(triedResponse))
    }
  }

  override def getCustomers(limit: Option[Int] = None,
                            offset: Option[Int] = None,
                            since: Option[ZonedDateTime] = None,
                            until: Option[ZonedDateTime] = None): Task[Collection[Customer]] = {
    val method = HttpMethods.GET
    val params = getParamMap(limit, offset, since, until)
    val uri    = Uri("/v1/customers").withQuery(Uri.Query(params))
    val responseFuture = Source
      .single(HttpRequest(uri = uri, method = method).addCredentials(BasicHttpCredentials(config.secretKey, "")) -> 1)
      .via(poolClientFlow)
      .runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        println(triedResponse)
        responseToModel[Collection[Customer]](Task.fromTry(triedResponse))
    }
  }

  // ---

//  def createCustomerCard(customerId: String)(cardToken: CardToken): Task[CustomerCard] = {
//    val method = HttpMethods.POST
//    val path   = s"/v1/customers/$customerId/cards"
//    val responseFuture = Source
//      .single(HttpRequest(uri = path, method = method).addCredentials(BasicHttpCredentials(config.secretKey, "")) -> 1)
//      .via(poolClientFlow)
//      .runWith(Sink.head)
//    Task.fromFuture(responseFuture).flatMap {
//      case (triedResponse, _) =>
//        println(triedResponse)
//        responseToModel[CustomerCard](Task.fromTry(triedResponse))
//    }
//  }

  // --- 顧客定期課金情報

  override def getCustomerSubscription(customerId: CustomerId, subscriptionId: SubscriptionId): Task[Subscription] = {
    val method         = HttpMethods.GET
    val path           = s"/v1/customers/${customerId.value}/subscriptions/${subscriptionId.value}"
    val request        = HttpRequest(uri = path, method = method).addCredentials(BasicHttpCredentials(config.secretKey, ""))
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        println(triedResponse)
        responseToModel[Subscription](Task.fromTry(triedResponse))
    }
  }

  // --- 顧客定期課金情報リスト
  override def getCustomerSubscriptions(customerId: CustomerId,
                                        planId: Option[PlanId] = None,
                                        status: Option[SubscriptionStatus] = None,
                                        limit: Option[Int] = None,
                                        offset: Option[Int] = None,
                                        since: Option[ZonedDateTime] = None,
                                        until: Option[ZonedDateTime] = None): Task[Collection[Subscription]] = {
    val method = HttpMethods.GET
    val params = getParamMap(limit, offset, since, until) ++
    planId.map(v => Map("plan"   -> v.value)).getOrElse(Map.empty) ++
    status.map(v => Map("status" -> v.entryName)).getOrElse(Map.empty)
    val uri            = Uri(s"/v1/customers/${customerId.value}/subscriptions").withQuery(Uri.Query(params))
    val request        = HttpRequest(uri = uri, method = method).addCredentials(BasicHttpCredentials(config.secretKey, ""))
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Collection[Subscription]](Task.fromTry(triedResponse))
    }
  }

  // ---　プラン

  override def createPlan(amount: Amount,
                          currency: Currency,
                          interval: PlanInterval,
                          idOpt: Option[String] = None,
                          nameOpt: Option[String] = None,
                          trialDaysOpt: Option[Int] = None,
                          billingDayOpt: Option[Int] = None,
                          metaData: Map[String, String] = Map.empty): Task[Plan] = {
    val method = HttpMethods.POST
    val path   = s"/v1/plans"
    val params = Map("amount" -> amount.value.toString(),
                     "currency" -> currency.value,
                     "interval" -> interval.entryName) ++
    idOpt.map(v => Map("id"                  -> v)).getOrElse(Map.empty) ++
    nameOpt.map(v => Map("name"              -> v)).getOrElse(Map.empty) ++
    trialDaysOpt.map(v => Map("trial_days"   -> v.toString)).getOrElse(Map.empty) ++
    billingDayOpt.map(v => Map("billing_day" -> v.toString)).getOrElse(Map.empty) ++
    metaData
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
      .withEntity(FormData(params).toEntity)
    val responseFuture = Source
      .single(request -> 1)
      .via(poolClientFlow)
      .runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Plan](Task.fromTry(triedResponse))
    }
  }

  override def getPlan(planId: PlanId): Task[Plan] = {
    val method         = HttpMethods.GET
    val path           = s"/v1/plans/${planId.value}"
    val request        = HttpRequest(uri = path, method = method).addCredentials(BasicHttpCredentials(config.secretKey, ""))
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        println(triedResponse)
        responseToModel[Plan](Task.fromTry(triedResponse))
    }
  }

  override def updatePlan(planId: PlanId,
                          nameOpt: Option[String] = None,
                          metaData: Map[String, String] = Map.empty): Task[Plan] = {
    val method = HttpMethods.POST
    val path   = s"/v1/plans/${planId.value}"
    val params = nameOpt.map(v => Map("name" -> v)).getOrElse(Map.empty) ++
    metaData
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
      .withEntity(FormData(params).toEntity)
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Plan](Task.fromTry(triedResponse))
    }
  }

  override def deletePlan(planId: PlanId): Task[Deleted[String]] = {
    val method = HttpMethods.DELETE
    val path   = s"/v1/plans/${planId.value}"
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Deleted[String]](Task.fromTry(triedResponse))
    }
  }

  override def getPlans(limit: Option[Int] = None,
                        offset: Option[Int] = None,
                        since: Option[ZonedDateTime] = None,
                        until: Option[ZonedDateTime] = None): Task[Collection[Plan]] = {
    val method = HttpMethods.GET
    val params = getParamMap(limit, offset, since, until)
    val uri    = Uri("/v1/plans").withQuery(Uri.Query(params))
    val responseFuture = Source
      .single(HttpRequest(uri = uri, method = method).addCredentials(BasicHttpCredentials(config.secretKey, "")) -> 1)
      .via(poolClientFlow)
      .runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Collection[Plan]](Task.fromTry(triedResponse))
    }
  }

  // --- 定期課金

  override def createSubscription(customerId: String,
                                  planId: PlanId,
                                  trialEnd: Option[ZonedDateTime] = None,
                                  prorate: Option[Boolean] = None,
                                  metaData: Map[String, String] = Map.empty): Task[Subscription] = {
    val method = HttpMethods.POST
    val path   = s"/v1/subscriptions"
    val params = Map("customer" -> customerId, "plan" -> planId.value) ++
    trialEnd.map(v => Map("trial_end" -> v.toEpochSecond.toString)).getOrElse(Map.empty) ++
    prorate.map(v => Map("prorate"    -> v.toString)).getOrElse(Map.empty) ++
    metaData
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
      .withEntity(FormData(params).toEntity)
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        println(triedResponse)
        responseToModel[Subscription](Task.fromTry(triedResponse))
    }
  }

  override def updateSubscription(subscriptionId: SubscriptionId,
                                  planId: Option[PlanId] = None,
                                  trialEnd: Option[ZonedDateTime] = None,
                                  prorate: Option[Boolean] = None,
                                  metaData: Map[String, String] = Map.empty): Task[Subscription] = {
    val method = HttpMethods.POST
    val path   = s"/v1/subscriptions/$subscriptionId"
    val params = planId.map(v => Map("plan" -> v.value)).getOrElse(Map.empty) ++
    trialEnd.map(v => Map("trial_end" -> v.toEpochSecond.toString)).getOrElse(Map.empty) ++
    prorate.map(v => Map("prorate"    -> v.toString)).getOrElse(Map.empty) ++
    metaData
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
      .withEntity(FormData(params).toEntity)
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Subscription](Task.fromTry(triedResponse))
    }
  }

  override def pauseSubscription(subscriptionId: SubscriptionId): Task[Subscription] = {
    val method = HttpMethods.POST
    val path   = s"/v1/subscriptions/$subscriptionId/pause"
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Subscription](Task.fromTry(triedResponse))
    }
  }

  override def resumeSubscription(subscriptionId: SubscriptionId): Task[Subscription] = {
    val method = HttpMethods.POST
    val path   = s"/v1/subscriptions/$subscriptionId/resume"
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Subscription](Task.fromTry(triedResponse))
    }
  }

  override def cancelSubscription(subscriptionId: SubscriptionId): Task[Subscription] = {
    val method = HttpMethods.POST
    val path   = s"/v1/subscriptions/$subscriptionId/cancel"
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Subscription](Task.fromTry(triedResponse))
    }
  }

  override def deleteSubscription(subscriptionId: SubscriptionId): Task[Deleted[String]] = {
    val method = HttpMethods.DELETE
    val path   = s"/v1/subscriptions/$subscriptionId"
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Deleted[String]](Task.fromTry(triedResponse))
    }
  }

  override def getSubscriptions(planId: Option[PlanId] = None,
                                status: Option[SubscriptionStatus] = None,
                                limit: Option[Int] = None,
                                offset: Option[Int] = None,
                                since: Option[ZonedDateTime] = None,
                                until: Option[ZonedDateTime] = None): Task[Collection[Subscription]] = {
    val method = HttpMethods.GET
    val params = getParamMap(limit, offset, since, until) ++
    planId.map(v => Map("plan"   -> v.value)).getOrElse(Map.empty) ++
    status.map(v => Map("status" -> v.entryName)).getOrElse(Map.empty)
    val uri = Uri("/v1/subscriptions").withQuery(Uri.Query(params))
    val responseFuture = Source
      .single(HttpRequest(uri = uri, method = method).addCredentials(BasicHttpCredentials(config.secretKey, "")) -> 1)
      .via(poolClientFlow)
      .runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Collection[Subscription]](Task.fromTry(triedResponse))
    }
  }

  // ---

  override def createCharge(amountAndCurrency: Option[(Amount, Currency)],
                            productId: Option[String],
                            customerId: Option[CustomerId],
                            cardToken: Option[CardToken],
                            description: Option[String] = None,
                            capture: Option[Boolean] = None,
                            expiryDays: Option[ZonedDateTime] = None,
                            metaData: Map[String, String] = Map.empty,
                            platformFee: Option[BigDecimal] = None): Task[Charge] = {
    require(
      (amountAndCurrency, productId) match {
        case (Some(_), None) => true
        case (None, Some(_)) => true
        case _               => false
      },
      "amount and currency or productId are required"
    )
    require((customerId, cardToken) match {
      case (Some(_), None) => true
      case (None, Some(_)) => true
      case _               => false
    }, "customer or cardToken are required")
    val method = HttpMethods.POST
    val path   = s"/v1/charges"
    val params =
    amountAndCurrency
      .map { case (a, c) => Map("amount" -> a.value.toString, "currency" -> c.value) }
      .getOrElse(Map.empty) ++
    productId.map(v => Map("product"       -> v)).getOrElse(Map.empty) ++
    customerId.map(v => Map("customer"     -> v.value)).getOrElse(Map.empty) ++
    cardToken.map(v => Map("card"          -> v.value.toString)).getOrElse(Map.empty) ++
    description.map(v => Map("description" -> v)).getOrElse(Map.empty) ++
    capture.map(v => Map("capture"         -> v.toString)).getOrElse(Map.empty) ++
    expiryDays.map(v => Map("expiry_days"  -> v.toEpochSecond.toString)).getOrElse(Map.empty) ++
    metaData
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      } ++
    platformFee.map(v => Map("platform_fee" -> v.toString)).getOrElse(Map.empty)
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
      .withEntity(FormData(params).toEntity)
    val responseFuture = Source
      .single(request -> 1)
      .via(poolClientFlow)
      .runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Charge](Task.fromTry(triedResponse))
    }
  }

  /**
    * 支払いの取得
    *
    * @param chargeId
    * @return
    */
  override def getCharge(chargeId: ChargeId): Task[Charge] = {
    val method         = HttpMethods.GET
    val path           = s"/v1/charges/${chargeId.value}"
    val request        = HttpRequest(uri = path, method = method).addCredentials(BasicHttpCredentials(config.secretKey, ""))
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Charge](Task.fromTry(triedResponse))
    }
  }

  /**
    * 支払いの更新。
    *
    * @param chargeId 支払いID
    * @param description 説明　
    * @param metaData メタデータ
    * @return
    */
  override def updateCharge(chargeId: ChargeId,
                            description: Option[String] = None,
                            metaData: Map[String, String] = Map.empty): Task[Charge] = {
    val method = HttpMethods.POST
    val path   = s"/v1/charges/${chargeId.value}"
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Charge](Task.fromTry(triedResponse))
    }
  }

  /**
    * 返金する。
    *
    * @param chargeId チャージID
    * @param amount
    * @param refundReason
    * @return
    */
  override def refundCharge(chargeId: ChargeId,
                            amount: Option[Amount] = None,
                            refundReason: Option[String] = None): Task[Charge] = {
    val method = HttpMethods.POST
    val path   = s"/v1/charges/${chargeId.value}/refund"
    val params =
    amount
      .map(v => Map("amount" -> v.value.toString))
      .getOrElse(Map.empty) ++
    refundReason.map(v => Map("refund_reason" -> v)).getOrElse(Map.empty)
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
      .withEntity(FormData(params).toEntity)
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Charge](Task.fromTry(triedResponse))
    }
  }

  override def reauthCharge(chargeId: ChargeId, expiryDays: Option[Int] = None): Task[Charge] = {
    val method = HttpMethods.POST
    val path   = s"/v1/charges/${chargeId.value}/reauth"
    val params =
      expiryDays
        .map(v => Map("expiry_days" -> v.toString))
        .getOrElse(Map.empty)
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
      .withEntity(FormData(params).toEntity)
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Charge](Task.fromTry(triedResponse))
    }
  }

  override def captureCharge(chargeId: ChargeId, amount: Option[Amount] = None): Task[Charge] = {
    val method = HttpMethods.POST
    val path   = s"/v1/charges/${chargeId.value}/capture"
    val params =
      amount
        .map(v => Map("expiry_days" -> v.value.toString))
        .getOrElse(Map.empty)
    val request = HttpRequest(uri = path, method = method)
      .addCredentials(BasicHttpCredentials(config.secretKey, ""))
      .withEntity(FormData(params).toEntity)
    val responseFuture = Source.single(request -> 1).via(poolClientFlow).runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Charge](Task.fromTry(triedResponse))
    }
  }

  def getCharges(customerId: Option[CustomerId] = None,
                 subscriptionId: Option[String] = None,
                 limit: Option[Int] = None,
                 offset: Option[Int] = None,
                 since: Option[ZonedDateTime] = None,
                 until: Option[ZonedDateTime] = None): Task[Collection[Subscription]] = {
    val method = HttpMethods.GET
    val params = getParamMap(limit, offset, since, until) ++
    customerId.map(v => Map("customer"   -> v.value)).getOrElse(Map.empty) ++
    subscriptionId.map(v => Map("status" -> v)).getOrElse(Map.empty)
    val uri = Uri("/v1/charges").withQuery(Uri.Query(params))
    val responseFuture = Source
      .single(HttpRequest(uri = uri, method = method).addCredentials(BasicHttpCredentials(config.secretKey, "")) -> 1)
      .via(poolClientFlow)
      .runWith(Sink.head)
    Task.fromFuture(responseFuture).flatMap {
      case (triedResponse, _) =>
        responseToModel[Collection[Subscription]](Task.fromTry(triedResponse))
    }
  }
}
