package com.github.j5ik2o.payjp.scala.model.merchant

import cats.Eq

case class ProductName(value: String, readingName: String, englishName: String)

object ProductName {

  implicit object ProductNameEq extends Eq[ProductName] {
    override def eqv(x: ProductName, y: ProductName): Boolean = x == y
  }

}
