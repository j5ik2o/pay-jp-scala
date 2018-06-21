package com.github.j5ik2o.payjp.scala.model

import io.circe.Decoder

case class PlanId(value: String) {
  require(value.startsWith("pln_"))
}

object PlanId {
  implicit val PlanIdDecoder: Decoder[PlanId] = Decoder.decodeString.map(PlanId(_))
}

case class Plan(id: PlanId,
                liveMode: Boolean,
                amount: Amount,
                currency: Currency,
                interval: PlanInterval,
                nameOpt: Option[String],
                trialDaysOpt: Option[Int],
                billingDayOpt: Option[Int],
                metadata: Map[String, String])

object Plan {

  implicit val PlanDecoder: Decoder[Plan] =
    Decoder.forProduct9("id",
                        "livemode",
                        "amount",
                        "currency",
                        "interval",
                        "name",
                        "trial_days",
                        "billing_day",
                        "metadata")(
      Plan.apply
    )

}
