package com.github.j5ik2o.payjp.scala.model.merchant

import cats.Eq

case class ProductPrice(min: Int, max: Int)

object ProductPrice {

  implicit object ProductPriceEq extends Eq[ProductPrice] {
    override def eqv(x: ProductPrice, y: ProductPrice): Boolean = x == y
  }

}
