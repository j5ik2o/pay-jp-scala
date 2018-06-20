package com.github.j5ik2o.payjp.scala.model

import io.circe.Decoder

case class Currency(value: String) {
  require(value == "jpy")
}

object Currency {
  implicit val CurrencyDecoder: Decoder[Currency] = Decoder[String].map(Currency(_))
}
