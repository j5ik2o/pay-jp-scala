package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import cats.Eq
import io.circe.Decoder

case class ChargeId(value: String)

object ChargeId {
  implicit val ChargeIdDecoder: Decoder[ChargeId] = Decoder.decodeString.map(ChargeId(_))
}

case class Charge(id: ChargeId,
                  liveMode: Boolean,
                  created: ZonedDateTime,
                  amount: Amount,
                  currency: Currency,
                  paid: Boolean,
                  expiredAt: Option[ZonedDateTime],
                  captured: Boolean,
                  capturedAt: ZonedDateTime,
                  customerId: Option[String],
                  description: Option[String],
                  failureCode: Option[String],
                  failureMessage: Option[String],
                  refunded: Boolean,
                  amountRefunded: BigDecimal,
                  refundReason: Option[String],
                  subscriptionId: Option[String],
                  metadata: Map[String, String],
                  platformFee: Option[BigDecimal])

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
