package com.github.j5ik2o.payjp.scala.model.merchant

import cats.Eq

case class ProductDetail(value: String, document: Seq[String] = Seq.empty)

object ProductDetail {

  implicit object ProductDetailEq extends Eq[ProductDetail] {
    override def eqv(x: ProductDetail, y: ProductDetail): Boolean = x == y
  }

}
