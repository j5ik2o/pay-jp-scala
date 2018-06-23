package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import cats.Eq
import io.circe.Decoder

case class SubscriptionId(value: String)

object SubscriptionId {
  implicit object SubscriptionEq extends Eq[Subscription] {
    override def eqv(x: Subscription, y: Subscription): Boolean = x == y
  }
  implicit val SubscriptionIdDecoder: Decoder[SubscriptionId] = Decoder.decodeString.map(SubscriptionId(_))
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
  * @param trialStart
  * @param trialEnd
  * @param pausedAt
  * @param canceledAt
  * @param resumedAt
  * @param metadata
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
                        trialStart: Option[ZonedDateTime],
                        trialEnd: Option[ZonedDateTime],
                        pausedAt: Option[ZonedDateTime],
                        canceledAt: Option[ZonedDateTime],
                        resumedAt: Option[ZonedDateTime],
                        metadata: Option[Map[String, String]])

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
