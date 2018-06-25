package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import cats.Eq
import io.circe.Decoder

case class ChargeId(value: String)

object ChargeId {

  implicit object ChargeIdEq extends Eq[ChargeId] {
    override def eqv(x: ChargeId, y: ChargeId): Boolean = x == y
  }

  implicit val ChargeIdDecoder: Decoder[ChargeId] = Decoder[String].map(ChargeId(_))

}

/**
  * 支払い。
  *
  * @param id
  * @param liveMode
  * @param created
  * @param amount
  * @param currency
  * @param paid
  * @param expiredAtOpt
  * @param captured
  * @param capturedAtOpt
  * @param customerIdOpt
  * @param descriptionOpt
  * @param failureCodeOpt
  * @param failureMessageOpt
  * @param refunded
  * @param amountRefunded
  * @param refundReasonOpt
  * @param subscriptionIdOpt
  * @param metadataOpt
  * @param platformFeeOpt
  */
case class Charge(id: ChargeId,
                  liveMode: Boolean,
                  created: ZonedDateTime,
                  amount: Amount,
                  currency: Currency,
                  paid: Boolean,
                  expiredAtOpt: Option[ZonedDateTime],
                  captured: Boolean,
                  capturedAtOpt: Option[ZonedDateTime],
                  customerIdOpt: Option[String],
                  descriptionOpt: Option[String],
                  failureCodeOpt: Option[String],
                  failureMessageOpt: Option[String],
                  refunded: Boolean,
                  amountRefunded: BigDecimal,
                  refundReasonOpt: Option[String],
                  subscriptionIdOpt: Option[String],
                  metadataOpt: Option[Map[String, String]],
                  platformFeeOpt: Option[BigDecimal])

object Charge extends JsonImplicits {

  implicit object ChargeEq extends Eq[Charge] {
    override def eqv(x: Charge, y: Charge): Boolean = x == y
  }

  implicit val ChargeDecoder: Decoder[Charge] =
    Decoder.forProduct19(
      "id",
      "livemode",
      "created",
      "amount",
      "currency",
      "paid",
      "expired_at",
      "captured",
      "captured_at",
      "customer",
      "description",
      "failure_code",
      "failure_message",
      "refunded",
      "amount_refunded",
      "refund_reason",
      "subscription",
      "metadata",
      "platform_fee"
    )(
      Charge.apply
    )

}
