package com.github.j5ik2o.payjp.scala.model

import cats.Eq
import io.circe.Decoder

case class PlanId(value: String) {
  require(value.startsWith("pln_"))
}

object PlanId {

  implicit object PlanIdEq extends Eq[PlanId] {
    override def eqv(x: PlanId, y: PlanId): Boolean = x == y
  }

  implicit val PlanIdDecoder: Decoder[PlanId] = Decoder[String].map(PlanId(_))
}

/**
  * 定期購入のためのプラン。
  *
  * @see https://pay.jp/docs/api/#plan-%E3%83%97%E3%83%A9%E3%83%B3
  *
  * @param id
  * @param liveMode
  * @param amount
  * @param currency
  * @param interval
  * @param nameOpt
  * @param trialDaysOpt
  * @param billingDayOpt
  * @param metadataOpt
  */
case class Plan(id: PlanId,
                liveMode: Boolean,
                amount: Amount,
                currency: Currency,
                interval: PlanIntervalType,
                nameOpt: Option[String],
                trialDaysOpt: Option[Int],
                billingDayOpt: Option[Int],
                metadataOpt: Option[Map[String, String]])

object Plan {

  implicit object PlanEq extends Eq[Plan] {
    override def eqv(x: Plan, y: Plan): Boolean = x == y
  }

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
