package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import cats.Eq
import io.circe.Decoder

case class SubscriptionId(value: String)

object SubscriptionId {
  implicit object SubscriptionEq extends Eq[Subscription] {
    override def eqv(x: Subscription, y: Subscription): Boolean = x == y
  }
  implicit val SubscriptionIdDecoder: Decoder[SubscriptionId] = Decoder[String].map(SubscriptionId(_))
}

/**
  * 定期課金。
  *
  * @param id
  * @param status
  * @param liveMode
  * @param created
  * @param start
  * @param customerId
  * @param plan
  * @param prorate
  * @param currentPeriodStart
  * @param currentPeriodEnd
  * @param trialStartOpt
  * @param trialEndOpt
  * @param pausedAtOpt
  * @param canceledAtOpt
  * @param resumedAtOpt
  * @param metadataOpt
  */
case class Subscription(id: SubscriptionId,
                        status: SubscriptionStatusType,
                        liveMode: Boolean,
                        created: ZonedDateTime,
                        start: ZonedDateTime,
                        customerId: String,
                        plan: Plan,
                        prorate: Boolean,
                        currentPeriodStart: ZonedDateTime,
                        currentPeriodEnd: ZonedDateTime,
                        trialStartOpt: Option[ZonedDateTime],
                        trialEndOpt: Option[ZonedDateTime],
                        pausedAtOpt: Option[ZonedDateTime],
                        canceledAtOpt: Option[ZonedDateTime],
                        resumedAtOpt: Option[ZonedDateTime],
                        metadataOpt: Option[Map[String, String]])

object Subscription extends JsonImplicits {

  implicit object SubscriptionEq extends Eq[Subscription] {
    override def eqv(x: Subscription, y: Subscription): Boolean = x == y
  }

  implicit val SubscriptionStatusDecoder: Decoder[Subscription] =
    Decoder.forProduct16(
      "id",
      "status",
      "livemode",
      "created",
      "start",
      "customer",
      "plan",
      "prorate",
      "current_period_start",
      "current_period_end",
      "trial_start",
      "trial_end",
      "paused_at",
      "canceled_at",
      "resumed_at",
      "metadata"
    )(Subscription.apply)

}
