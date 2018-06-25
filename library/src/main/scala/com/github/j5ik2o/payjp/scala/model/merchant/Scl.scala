package com.github.j5ik2o.payjp.scala.model.merchant

import cats.Eq

case class Scl(businessName: String,
               contactPersonName: String,
               email: String,
               phone: String,
               address: String,
               otherFee: Int,
               paymentMethod: String,
               timeOfPayment: String,
               timeOfDelivery: String,
               returnAndExchange: String,
               url: String)

object Scl {

  implicit object SclEq extends Eq[Scl] {
    override def eqv(x: Scl, y: Scl): Boolean = x == y
  }

}
