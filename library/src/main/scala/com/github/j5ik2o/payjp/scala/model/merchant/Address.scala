package com.github.j5ik2o.payjp.scala.model.merchant

import cats.Eq

case class Address(zip: String, state: Name, city: Name, line1: Name, line2: Name)

object Address {

  implicit object AddressEq extends Eq[Address] {
    override def eqv(x: Address, y: Address): Boolean = x == y
  }

}
