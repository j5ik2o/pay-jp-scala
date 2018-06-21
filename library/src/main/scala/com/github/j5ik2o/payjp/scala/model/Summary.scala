package com.github.j5ik2o.payjp.scala.model

import io.circe.Decoder

case class Summary(chargeCount: Int, chargeFee: Int, chargeGross: Int, net: Int, refundAmount: Int, refundCount: Int)

object Summary {
  implicit val SummaryDecoder: Decoder[Summary] =
    Decoder.forProduct6("charge_count", "charge_fee", "charge_gross", "net", "refund_amount", "refund_count")(
      Summary.apply
    )
}
