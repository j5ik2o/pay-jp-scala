package com.github.j5ik2o.payjp.scala.model

case class Currency(_value: String) {
  private val toJava = java.util.Currency.getInstance(_value)
  val value          = toJava.getCurrencyCode.toLowerCase
  require(value == "jpy")
}
