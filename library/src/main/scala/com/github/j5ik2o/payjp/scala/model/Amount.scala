package com.github.j5ik2o.payjp.scala.model

import cats.Eq
import io.circe.Decoder

case class Amount(value: BigDecimal) {
  // require(value.toLong >= 50L && value.toLong <= 9999999L, s"Failed to invalid value: $value")
}

object Amount {

  implicit object AmountEq extends Eq[Amount] {
    override def eqv(x: Amount, y: Amount): Boolean = x == y
  }

  implicit val PlanAmountDecoder: Decoder[Amount] = Decoder.decodeLong.map(Amount(_))

}
