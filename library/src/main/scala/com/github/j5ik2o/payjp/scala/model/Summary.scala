package com.github.j5ik2o.payjp.scala.model

import cats.Eq
import io.circe.Decoder

case class Summary(chargeCount: Int, chargeFee: Int, chargeGross: Int, net: Int, refundAmount: Int, refundCount: Int)

object Summary {
  implicit object SummaryEq extends Eq[Summary] {
    override def eqv(x: Summary, y: Summary): Boolean = x == y
  }
  implicit val SummaryDecoder: Decoder[Summary] =
    Decoder.forProduct6("charge_count", "charge_fee", "charge_gross", "net", "refund_amount", "refund_count")(
      Summary.apply
    )
}
