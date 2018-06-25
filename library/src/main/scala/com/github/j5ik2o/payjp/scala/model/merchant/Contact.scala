package com.github.j5ik2o.payjp.scala.model.merchant

import cats.Eq

case class Contact(phone: String, cellPhone: String)

object Contact {

  implicit object ContactEq extends Eq[Contact] {
    override def eqv(x: Contact, y: Contact): Boolean = x == y
  }

}
