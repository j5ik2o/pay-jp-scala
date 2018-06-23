package com.github.j5ik2o.payjp.scala.model

import cats.Eq
import io.circe.Decoder

case class Currency(value: String) {
  require(value == "jpy")
}

object Currency {
  implicit object CurrencyEq extends Eq[Currency] {
    override def eqv(x: Currency, y: Currency): Boolean = x == y
  }
  implicit val CurrencyDecoder: Decoder[Currency] = Decoder[String].map(Currency(_))
}
