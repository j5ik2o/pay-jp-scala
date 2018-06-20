package com.github.j5ik2o.payjp.scala.model

case class PlanAmount(value: BigDecimal) {
  require(value.toLong >= 50L && value.toLong <= 9999999L)
}
