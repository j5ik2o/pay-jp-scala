package com.github.j5ik2o.payjp.scala.model.merchant

import cats.Eq

case class Name(value: String, readingName: String)

object Name {

  implicit object NameEq extends Eq[Name] {
    override def eqv(x: Name, y: Name): Boolean = x == y
  }

}
