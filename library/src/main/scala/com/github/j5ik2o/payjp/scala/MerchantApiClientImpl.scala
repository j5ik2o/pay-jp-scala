package com.github.j5ik2o.payjp.scala
import java.time.ZonedDateTime

import akka.http.scaladsl.model._
import com.github.j5ik2o.payjp.scala.model._
import monix.eval.Task

class MerchantApiClientImpl(val sender: HttpRequestSender, secretKey: SecretKey)
    extends MerchantApiClient
    with ApiClientSupport
    with QueryBuildSupport {

  // --- 顧客
  override def createCustomer(customerIdOpt: Option[CustomerId],
                              emailOpt: Option[String],
                              descriptionOpt: Option[String],
                              tokenIdOpt: Option[TokenId],
                              metadata: Map[String, Any]): Task[Customer] = {
    val method = HttpMethods.POST
    val path   = s"/v1/customers"
    val params = customerIdOpt.map(v => Map("id" -> v.value)).getOrElse(Map.empty) ++
    emailOpt.map(v => Map("email"             -> v)).getOrElse(Map.empty) ++
    descriptionOpt.map(v => Map("description" -> v)).getOrElse(Map.empty) ++
    tokenIdOpt.map(v => Map("card"            -> v.value)).getOrElse(Map.empty) ++
    metadata
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[Customer](request, secretKey.value)
  }

  override def getCustomer(customerId: CustomerId): Task[Customer] = {
    val method  = HttpMethods.GET
    val path    = s"/v1/customers/${customerId.value}"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Customer](request, secretKey.value)
  }

  override def updateCustomer(customerId: CustomerId,
                              emailOpt: Option[String],
                              descriptionOpt: Option[String],
                              defaultCardOpt: Option[String],
                              cardOpt: Option[TokenId],
                              metadata: Map[String, String]): Task[Customer] = {
    val method = HttpMethods.POST
    val path   = s"/v1/customers/${customerId.value}"
    val params = emailOpt.map(v => Map("email" -> v)).getOrElse(Map.empty) ++
    descriptionOpt.map(v => Map("description"  -> v)).getOrElse(Map.empty) ++
    defaultCardOpt.map(v => Map("default_card" -> v)).getOrElse(Map.empty) ++
    cardOpt.map(v => Map("card"                -> v.value)).getOrElse(Map.empty) ++
    metadata
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[Customer](request, secretKey.value)
  }

  override def deleteCustomer(customerId: CustomerId): Task[Deleted[CustomerId]] = {
    val method  = HttpMethods.DELETE
    val path    = s"/v1/customers/${customerId.value}"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Deleted[CustomerId]](request, secretKey.value)
  }

  override def getCustomers(limit: Option[Int],
                            offset: Option[Int],
                            since: Option[ZonedDateTime],
                            until: Option[ZonedDateTime]): Task[Collection[Customer]] = {
    val method  = HttpMethods.GET
    val params  = getParamMap(limit, offset, since, until)
    val uri     = Uri("/v1/customers").withQuery(Uri.Query(params))
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Collection[Customer]](request, secretKey.value)
  }

  // --- 顧客定期課金情報

  override def getSubscriptionByCustomerId(customerId: CustomerId,
                                           subscriptionId: SubscriptionId): Task[Subscription] = {
    val method  = HttpMethods.GET
    val path    = s"/v1/customers/${customerId.value}/subscriptions/${subscriptionId.value}"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Subscription](request, secretKey.value)
  }

  // --- 顧客定期課金情報リスト
  override def getSubscriptionsByCustomerId(customerId: CustomerId,
                                            planIdOpt: Option[PlanId],
                                            statusOpt: Option[SubscriptionStatusType],
                                            limit: Option[Int],
                                            offset: Option[Int],
                                            since: Option[ZonedDateTime],
                                            until: Option[ZonedDateTime]): Task[Collection[Subscription]] = {
    val method = HttpMethods.GET
    val params = getParamMap(limit, offset, since, until) ++
    planIdOpt.map(v => Map("plan"   -> v.value)).getOrElse(Map.empty) ++
    statusOpt.map(v => Map("status" -> v.entryName)).getOrElse(Map.empty)
    val uri     = Uri(s"/v1/customers/${customerId.value}/subscriptions").withQuery(Uri.Query(params))
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Collection[Subscription]](request, secretKey.value)
  }

  // ---　プラン

  override def createPlan(amount: Amount,
                          currency: Currency,
                          interval: PlanIntervalType,
                          idOpt: Option[String],
                          nameOpt: Option[String],
                          trialDaysOpt: Option[Int],
                          billingDayOpt: Option[Int],
                          metadata: Map[String, String]): Task[Plan] = {
    val method = HttpMethods.POST
    val path   = s"/v1/plans"
    val params = Map("amount" -> amount.value.toString(),
                     "currency" -> currency.value,
                     "interval" -> interval.entryName) ++
    idOpt.map(v => Map("id"                  -> v)).getOrElse(Map.empty) ++
    nameOpt.map(v => Map("name"              -> v)).getOrElse(Map.empty) ++
    trialDaysOpt.map(v => Map("trial_days"   -> v.toString)).getOrElse(Map.empty) ++
    billingDayOpt.map(v => Map("billing_day" -> v.toString)).getOrElse(Map.empty) ++
    metadata
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[Plan](request, secretKey.value)
  }

  override def getPlan(planId: PlanId): Task[Plan] = {
    val method  = HttpMethods.GET
    val path    = s"/v1/plans/${planId.value}"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Plan](request, secretKey.value)
  }

  override def updatePlan(planId: PlanId, nameOpt: Option[String], metadata: Map[String, String]): Task[Plan] = {
    val method = HttpMethods.POST
    val path   = s"/v1/plans/${planId.value}"
    val params = nameOpt.map(v => Map("name" -> v)).getOrElse(Map.empty) ++
    metadata
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[Plan](request, secretKey.value)
  }

  override def deletePlan(planId: PlanId): Task[Deleted[PlanId]] = {
    val method  = HttpMethods.DELETE
    val path    = s"/v1/plans/${planId.value}"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Deleted[PlanId]](request, secretKey.value)
  }

  override def getPlans(limit: Option[Int],
                        offset: Option[Int],
                        since: Option[ZonedDateTime],
                        until: Option[ZonedDateTime]): Task[Collection[Plan]] = {
    val method  = HttpMethods.GET
    val params  = getParamMap(limit, offset, since, until)
    val uri     = Uri("/v1/plans").withQuery(Uri.Query(params))
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Collection[Plan]](request, secretKey.value)
  }

  // --- 定期課金

  override def createSubscription(customerId: CustomerId,
                                  planId: PlanId,
                                  trialEndOpt: Option[ZonedDateTime],
                                  prorateOpt: Option[Boolean],
                                  metadata: Map[String, String]): Task[Subscription] = {
    val method = HttpMethods.POST
    val path   = s"/v1/subscriptions"
    val params = Map("customer" -> customerId.value, "plan" -> planId.value) ++
    trialEndOpt.map(v => Map("trial_end" -> v.toEpochSecond.toString)).getOrElse(Map.empty) ++
    prorateOpt.map(v => Map("prorate"    -> v.toString)).getOrElse(Map.empty) ++
    metadata
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[Subscription](request, secretKey.value)
  }

  override def updateSubscription(subscriptionId: SubscriptionId,
                                  planIdOpt: Option[PlanId],
                                  trialEndOpt: Option[ZonedDateTime],
                                  prorateOpt: Option[Boolean],
                                  metadata: Map[String, String]): Task[Subscription] = {
    val method = HttpMethods.POST
    val path   = s"/v1/subscriptions/${subscriptionId.value}"
    val params = planIdOpt.map(v => Map("plan" -> v.value)).getOrElse(Map.empty) ++
    trialEndOpt.map(v => Map("trial_end" -> v.toEpochSecond.toString)).getOrElse(Map.empty) ++
    prorateOpt.map(v => Map("prorate"    -> v.toString)).getOrElse(Map.empty) ++
    metadata
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[Subscription](request, secretKey.value)
  }

  override def pauseSubscription(subscriptionId: SubscriptionId): Task[Subscription] = {
    val method  = HttpMethods.POST
    val path    = s"/v1/subscriptions/${subscriptionId.value}/pause"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Subscription](request, secretKey.value)
  }

  override def resumeSubscription(subscriptionId: SubscriptionId): Task[Subscription] = {
    val method  = HttpMethods.POST
    val path    = s"/v1/subscriptions/${subscriptionId.value}/resume"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Subscription](request, secretKey.value)
  }

  override def cancelSubscription(subscriptionId: SubscriptionId): Task[Subscription] = {
    val method  = HttpMethods.POST
    val path    = s"/v1/subscriptions/${subscriptionId.value}/cancel"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Subscription](request, secretKey.value)
  }

  override def deleteSubscription(subscriptionId: SubscriptionId): Task[Deleted[SubscriptionId]] = {
    val method  = HttpMethods.DELETE
    val path    = s"/v1/subscriptions/${subscriptionId.value}"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Deleted[SubscriptionId]](request, secretKey.value)
  }

  override def getSubscriptions(planIdOpt: Option[PlanId],
                                statusOpt: Option[SubscriptionStatusType],
                                limitOpt: Option[Int],
                                offsetOpt: Option[Int],
                                sinceOpt: Option[ZonedDateTime],
                                untilOpt: Option[ZonedDateTime]): Task[Collection[Subscription]] = {
    val method = HttpMethods.GET
    val params = getParamMap(limitOpt, offsetOpt, sinceOpt, untilOpt) ++
    planIdOpt.map(v => Map("plan"   -> v.value)).getOrElse(Map.empty) ++
    statusOpt.map(v => Map("status" -> v.entryName)).getOrElse(Map.empty)
    val uri     = Uri("/v1/subscriptions").withQuery(Uri.Query(params))
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Collection[Subscription]](request, secretKey.value)
  }

  // ---

  override def createCharge(amountAndCurrencyOpt: Option[(Amount, Currency)],
                            productIdOpt: Option[ProductId],
                            customerIdOpt: Option[CustomerId],
                            tokenIdOpt: Option[TokenId],
                            descriptionOpt: Option[String],
                            captureOpt: Option[Boolean],
                            expiryDaysOpt: Option[ZonedDateTime],
                            metadata: Map[String, String],
                            platformFeeOpt: Option[BigDecimal]): Task[Charge] = {
    require(
      (amountAndCurrencyOpt, productIdOpt) match {
        case (Some(_), None) => true
        case (None, Some(_)) => true
        case _               => false
      },
      "amount and currency or productId are required"
    )
    require((customerIdOpt, tokenIdOpt) match {
      case (Some(_), None) => true
      case (None, Some(_)) => true
      case _               => false
    }, "customer or cardToken are required")
    val method = HttpMethods.POST
    val path   = s"/v1/charges"
    val params =
    amountAndCurrencyOpt
      .map { case (a, c) => Map("amount" -> a.value.toString, "currency" -> c.value) }
      .getOrElse(Map.empty) ++
    productIdOpt.map(v => Map("product"       -> v.value)).getOrElse(Map.empty) ++
    customerIdOpt.map(v => Map("customer"     -> v.value)).getOrElse(Map.empty) ++
    tokenIdOpt.map(v => Map("card"            -> v.value.toString)).getOrElse(Map.empty) ++
    descriptionOpt.map(v => Map("description" -> v)).getOrElse(Map.empty) ++
    captureOpt.map(v => Map("capture"         -> v.toString)).getOrElse(Map.empty) ++
    expiryDaysOpt.map(v => Map("expiry_days"  -> v.toEpochSecond.toString)).getOrElse(Map.empty) ++
    metadata
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      } ++
    platformFeeOpt.map(v => Map("platform_fee" -> v.toString)).getOrElse(Map.empty)
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[Charge](request, secretKey.value)
  }

  override def getCharge(chargeId: ChargeId): Task[Charge] = {
    val method  = HttpMethods.GET
    val path    = s"/v1/charges/${chargeId.value}"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Charge](request, secretKey.value)
  }

  override def updateCharge(chargeId: ChargeId,
                            description: Option[String] = None,
                            metadata: Map[String, String] = Map.empty): Task[Charge] = {
    val method  = HttpMethods.POST
    val path    = s"/v1/charges/${chargeId.value}"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Charge](request, secretKey.value)
  }

  override def refundCharge(chargeId: ChargeId, amount: Option[Amount], refundReason: Option[String]): Task[Charge] = {
    val method = HttpMethods.POST
    val path   = s"/v1/charges/${chargeId.value}/refund"
    val params =
    amount
      .map(v => Map("amount" -> v.value.toString))
      .getOrElse(Map.empty) ++
    refundReason.map(v => Map("refund_reason" -> v)).getOrElse(Map.empty)
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[Charge](request, secretKey.value)
  }

  override def reAuthCharge(chargeId: ChargeId, expiryDays: Option[Int]): Task[Charge] = {
    val method = HttpMethods.POST
    val path   = s"/v1/charges/${chargeId.value}/reauth"
    val params =
      expiryDays
        .map(v => Map("expiry_days" -> v.toString))
        .getOrElse(Map.empty)
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[Charge](request, secretKey.value)
  }

  override def captureCharge(chargeId: ChargeId, amount: Option[Amount]): Task[Charge] = {
    val method = HttpMethods.POST
    val path   = s"/v1/charges/${chargeId.value}/capture"
    val params =
      amount
        .map(v => Map("expiry_days" -> v.value.toString))
        .getOrElse(Map.empty)
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[Charge](request, secretKey.value)
  }

  def getCharges(customerId: Option[CustomerId],
                 subscriptionId: Option[String],
                 limit: Option[Int],
                 offset: Option[Int],
                 since: Option[ZonedDateTime],
                 until: Option[ZonedDateTime]): Task[Collection[Charge]] = {
    val method = HttpMethods.GET
    val params = getParamMap(limit, offset, since, until) ++
    customerId.map(v => Map("customer"   -> v.value)).getOrElse(Map.empty) ++
    subscriptionId.map(v => Map("status" -> v)).getOrElse(Map.empty)
    val uri     = Uri("/v1/charges").withQuery(Uri.Query(params))
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Collection[Charge]](request, secretKey.value)
  }

  override def createProduct(amount: Amount,
                             currency: Currency,
                             invalidAfter: Option[ZonedDateTime],
                             capture: Option[Boolean],
                             metadata: Map[String, String]): Task[Product] = {
    val method = HttpMethods.POST
    val path   = s"/v1/products"
    val params = Map("amount" -> amount.value.toString(), "currency" -> currency.value) ++
    invalidAfter.map(v => Map("invalid_after" -> v.toEpochSecond.toString)).getOrElse(Map.empty) ++
    capture.map(v => Map("capture"            -> v.toString)).getOrElse(Map.empty) ++
    metadata
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[Product](request, secretKey.value)
  }

  override def getProduct(productId: ProductId): Task[Product] = {
    val method  = HttpMethods.GET
    val path    = s"/v1/products/${productId.value}"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Product](request, secretKey.value)
  }

  override def updateProduct(productId: ProductId,
                             invalidAfter: Option[ZonedDateTime],
                             metadata: Map[String, String]): Task[Product] = {
    val method = HttpMethods.POST
    val path   = s"/v1/products/${productId.value}"
    val params = invalidAfter.map(v => Map("invalid_after" -> v.toEpochSecond.toString)).getOrElse(Map.empty) ++
    metadata
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[Product](request, secretKey.value)
  }

  override def deleteProduct(productId: ProductId): Task[Deleted[ProductId]] = {
    val method  = HttpMethods.DELETE
    val path    = s"/v1/products/${productId.value}"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Deleted[ProductId]](request, secretKey.value)
  }

  override def getProducts(limit: Option[Int],
                           offset: Option[Int],
                           since: Option[ZonedDateTime],
                           until: Option[ZonedDateTime]): Task[Collection[Product]] = {
    val method  = HttpMethods.GET
    val params  = getParamMap(limit, offset, since, until)
    val uri     = Uri("/v1/products").withQuery(Uri.Query(params))
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Collection[Product]](request, secretKey.value)
  }

  override def getTransfer(transferId: TransferId): Task[Transfer] = {
    val method  = HttpMethods.GET
    val path    = s"/v1/transfers/${transferId.value}"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Transfer](request, secretKey.value)
  }

  override def getTransfers(status: Option[TransferStatusType],
                            limit: Option[Int],
                            offset: Option[Int],
                            since: Option[ZonedDateTime],
                            until: Option[ZonedDateTime]): Task[Collection[Transfer]] = {
    val method = HttpMethods.GET
    val params = getParamMap(limit, offset, since, until) ++
    status.map(v => Map("status" -> v.entryName)).getOrElse(Map.empty)
    val uri     = Uri("/v1/transfers").withQuery(Uri.Query(params))
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Collection[Transfer]](request, secretKey.value)
  }

  override def getChargesByTransferId(transferId: TransferId,
                                      customerId: Option[CustomerId],
                                      limit: Option[Int],
                                      offset: Option[Int],
                                      since: Option[ZonedDateTime],
                                      until: Option[ZonedDateTime]): Task[Collection[Charge]] = {
    val method = HttpMethods.GET
    val params = getParamMap(limit, offset, since, until) ++ customerId
      .map(v => Map("customer" -> v.value))
      .getOrElse(Map.empty)
    val uri     = Uri(s"/v1/transfers/${transferId.value}/charges").withQuery(Uri.Query(params))
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Collection[Charge]](request, secretKey.value)
  }

  override def getAccount(): Task[Account] = {
    val method  = HttpMethods.GET
    val uri     = Uri(s"/v1/accounts")
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Account](request, secretKey.value)
  }

  override def getEvent(eventId: EventId): Task[Event] = {
    val method  = HttpMethods.GET
    val path    = s"/v1/events/${eventId.value}"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Event](request, secretKey.value)
  }

  override def getEvents(resourceIdOpt: Option[String] = None,
                         objectOpt: Option[String] = None,
                         typeOpt: Option[String] = None,
                         limitOpt: Option[Int] = None,
                         offsetOpt: Option[Int] = None,
                         sinceOpt: Option[ZonedDateTime] = None,
                         untilOpt: Option[ZonedDateTime] = None): Task[Collection[Event]] = {
    val method = HttpMethods.GET
    val params = getParamMap(limitOpt, offsetOpt, sinceOpt, untilOpt) ++ resourceIdOpt
      .map(v => Map("resource_id"                              -> v))
      .getOrElse(Map.empty) ++ objectOpt.map(v => Map("object" -> v)).getOrElse(Map.empty) ++ typeOpt
      .map(v => Map("type"                                     -> v))
      .getOrElse(Map.empty)
    val uri     = Uri("/v1/events").withQuery(Uri.Query(params))
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Collection[Event]](request, secretKey.value)
  }

  def createTestToken(number: String,
                      expMonth: Int,
                      expYear: Int,
                      cvc: Option[String],
                      addressState: Option[String],
                      addressCity: Option[String],
                      addressLine1: Option[String],
                      addressLine2: Option[String],
                      addressZip: Option[String],
                      country: Option[String],
                      name: Option[String]): Task[Token] = {
    val method = HttpMethods.POST
    val path   = s"/v1/tokens"
    val params = Map("card[number]" -> number,
                     "card[exp_month]" -> expMonth.toString,
                     "card[exp_year]"  -> expYear.toString) ++
    cvc.map(v => Map("card[cvc]"                    -> v)).getOrElse(Map.empty) ++
    addressState.map(v => Map("card[address_state]" -> v)).getOrElse(Map.empty) ++
    addressCity.map(v => Map("card[address_city]"   -> v)).getOrElse(Map.empty) ++
    addressLine1.map(v => Map("card[address_line1]" -> v)).getOrElse(Map.empty) ++
    addressLine2.map(v => Map("card[address_line2]" -> v)).getOrElse(Map.empty) ++
    addressZip.map(v => Map("card[address_zip]"     -> v)).getOrElse(Map.empty) ++
    name.map(v => Map("card[name]"                  -> v)).getOrElse(Map.empty)
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[Token](request, secretKey.value, testHeaders)
  }

  override def getTestToken(tokenId: TokenId): Task[Token] = {
    val method  = HttpMethods.GET
    val uri     = Uri(s"/v1/tokens/${tokenId.value}")
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Token](request, secretKey.value, testHeaders)
  }

}
