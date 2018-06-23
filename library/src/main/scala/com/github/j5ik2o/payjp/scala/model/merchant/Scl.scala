package com.github.j5ik2o.payjp.scala.model.merchant

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
