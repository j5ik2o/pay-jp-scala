package com.github.j5ik2o.payjp.scala

import java.time.ZonedDateTime

import com.github.j5ik2o.payjp.scala.model._
import monix.eval.Task

trait MerchantApiClient {
  // --- Customer API
  def createCustomer(customerIdOpt: Option[CustomerId] = None,
                     emailOpt: Option[String] = None,
                     descriptionOpt: Option[String] = None,
                     cardTokenOpt: Option[CardToken] = None,
                     metadata: Map[String, Any] = Map.empty): Task[Customer]

  def getCustomer(customerId: CustomerId): Task[Customer]

  def updateCustomer(customerId: CustomerId,
                     emailOpt: Option[String] = None,
                     descriptionOpt: Option[String] = None,
                     defaultCardOpt: Option[String] = None,
                     cardOpt: Option[CardToken] = None,
                     metadata: Map[String, String] = Map.empty): Task[Customer]

  def deleteCustomer(customerId: CustomerId): Task[Deleted[CustomerId]]

  def getCustomers(limit: Option[Int] = None,
                   offset: Option[Int] = None,
                   since: Option[ZonedDateTime] = None,
                   until: Option[ZonedDateTime] = None): Task[Collection[Customer]]

  // --- Plan API
  def createPlan(amount: Amount,
                 currency: Currency,
                 interval: PlanInterval,
                 idOpt: Option[String] = None,
                 nameOpt: Option[String] = None,
                 trialDaysOpt: Option[Int] = None,
                 billingDayOpt: Option[Int] = None,
                 metadata: Map[String, String] = Map.empty): Task[Plan]

  def getPlan(planId: PlanId): Task[Plan]

  def updatePlan(planId: PlanId, nameOpt: Option[String] = None, metadata: Map[String, String] = Map.empty): Task[Plan]

  def deletePlan(planId: PlanId): Task[Deleted[PlanId]]

  def getPlans(limit: Option[Int] = None,
               offset: Option[Int] = None,
               since: Option[ZonedDateTime] = None,
               until: Option[ZonedDateTime] = None): Task[Collection[Plan]]

  // --- Subscription API
  def createSubscription(customerId: CustomerId,
                         planId: PlanId,
                         trialEnd: Option[ZonedDateTime] = None,
                         prorate: Option[Boolean] = None,
                         metadata: Map[String, String] = Map.empty): Task[Subscription]

  def updateSubscription(subscriptionId: SubscriptionId,
                         planId: Option[PlanId] = None,
                         trialEnd: Option[ZonedDateTime] = None,
                         prorate: Option[Boolean] = None,
                         metadata: Map[String, String] = Map.empty): Task[Subscription]

  def pauseSubscription(subscriptionId: SubscriptionId): Task[Subscription]

  def resumeSubscription(subscriptionId: SubscriptionId): Task[Subscription]

  def cancelSubscription(subscriptionId: SubscriptionId): Task[Subscription]

  def deleteSubscription(subscriptionId: SubscriptionId): Task[Deleted[SubscriptionId]]

  def getSubscriptions(planId: Option[PlanId] = None,
                       status: Option[SubscriptionStatus] = None,
                       limit: Option[Int] = None,
                       offset: Option[Int] = None,
                       since: Option[ZonedDateTime] = None,
                       until: Option[ZonedDateTime] = None): Task[Collection[Subscription]]

  // --- Customer Subscription API

  def getSubscriptionByCustomerId(customerId: CustomerId, subscriptionId: SubscriptionId): Task[Subscription]

  def getSubscriptionsByCustomerId(customerId: CustomerId,
                                   planId: Option[PlanId] = None,
                                   status: Option[SubscriptionStatus] = None,
                                   limit: Option[Int] = None,
                                   offset: Option[Int] = None,
                                   since: Option[ZonedDateTime] = None,
                                   until: Option[ZonedDateTime] = None): Task[Collection[Subscription]]

  // --- Charge API
  def createCharge(amountAndCurrency: Option[(Amount, Currency)],
                   productId: Option[ProductId],
                   customerId: Option[CustomerId],
                   cardToken: Option[CardToken],
                   description: Option[String] = None,
                   capture: Option[Boolean] = None,
                   expiryDays: Option[ZonedDateTime] = None,
                   metadata: Map[String, String] = Map.empty,
                   platformFee: Option[BigDecimal] = None): Task[Charge]

  def getCharge(chargeId: ChargeId): Task[Charge]

  def updateCharge(chargeId: ChargeId,
                   description: Option[String] = None,
                   metadata: Map[String, String] = Map.empty): Task[Charge]

  def refundCharge(chargeId: ChargeId, amount: Option[Amount] = None, refundReason: Option[String] = None): Task[Charge]

  def reAuthCharge(chargeId: ChargeId, expiryDays: Option[Int] = None): Task[Charge]

  def captureCharge(chargeId: ChargeId, amount: Option[Amount] = None): Task[Charge]

  def getCharges(customerId: Option[CustomerId] = None,
                 subscriptionId: Option[String] = None,
                 limit: Option[Int] = None,
                 offset: Option[Int] = None,
                 since: Option[ZonedDateTime] = None,
                 until: Option[ZonedDateTime] = None): Task[Collection[Charge]]

  def getChargesByTransferId(transferId: TransferId,
                             customerId: Option[CustomerId] = None,
                             limit: Option[Int] = None,
                             offset: Option[Int] = None,
                             since: Option[ZonedDateTime] = None,
                             until: Option[ZonedDateTime] = None): Task[Collection[Charge]]

  // --- Product API
  def createProduct(amount: Amount,
                    currency: Currency,
                    invalidAfter: Option[ZonedDateTime] = None,
                    capture: Option[Boolean] = None,
                    metadata: Map[String, String] = Map.empty): Task[Product]

  def getProduct(productId: ProductId): Task[Product]

  def updateProduct(productId: ProductId,
                    invalidAfter: Option[ZonedDateTime],
                    metadata: Map[String, String]): Task[Product]
  def deleteProduct(productId: ProductId): Task[Deleted[ProductId]]
  def getProducts(limit: Option[Int] = None,
                  offset: Option[Int] = None,
                  since: Option[ZonedDateTime] = None,
                  until: Option[ZonedDateTime] = None): Task[Collection[Product]]

  // --- Transfer API

  def getTransfer(transferId: TransferId): Task[Transfer]

  def getTransfers(status: Option[TransferStatus] = None,
                   limit: Option[Int] = None,
                   offset: Option[Int] = None,
                   since: Option[ZonedDateTime] = None,
                   until: Option[ZonedDateTime] = None): Task[Collection[Transfer]]

  def getAccount(): Task[Account]
}
