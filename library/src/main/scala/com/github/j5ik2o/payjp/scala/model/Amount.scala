package com.github.j5ik2o.payjp.scala.model

import io.circe.Decoder

case class Amount(value: BigDecimal) {
  require(value.toLong >= 50L && value.toLong <= 9999999L)
}

object Amount {

  implicit val PlanAmountDecoder: Decoder[Amount] = Decoder.decodeLong.map(Amount(_))

}
