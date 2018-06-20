package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import io.circe.Decoder

case class SubscriptionId(value: String)

object SubscriptionId {
  implicit val SubscriptionIdDecoder: Decoder[SubscriptionId] = Decoder.decodeString.map(SubscriptionId(_))
}

case class Subscription(id: SubscriptionId,
                        status: SubscriptionStatus,
                        liveMode: Boolean,
                        created: ZonedDateTime,
                        start: ZonedDateTime,
                        customerId: String,
                        plan: Plan,
                        prorate: Boolean,
                        currentPeriodStart: ZonedDateTime,
                        currentPeriodEnd: ZonedDateTime,
                        trialStart: ZonedDateTime,
                        trialEnd: ZonedDateTime,
                        pausedAt: ZonedDateTime,
                        canceledAt: ZonedDateTime,
                        resumedAt: ZonedDateTime,
                        metadata: Map[String, String]) {
  val `object` = "subscription"
}

object Subscription extends JsonImplicits {

  implicit val SubscriptionStatusDecoder: Decoder[Subscription] =
    Decoder.forProduct16(
      "id",
      "status",
      "livemode",
      "created",
      "start",
      "customer_id",
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
