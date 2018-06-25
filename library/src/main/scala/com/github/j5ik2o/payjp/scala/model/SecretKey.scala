package com.github.j5ik2o.payjp.scala.model

import cats.Eq
import io.circe.Decoder

case class SecretKey(value: String)

object SecretKey {

  implicit object SecretKeyEq extends Eq[SecretKey] {
    override def eqv(x: SecretKey, y: SecretKey): Boolean = x == y
  }

  implicit val SecretKeyDecoder: Decoder[SecretKey] = Decoder[String].map(SecretKey(_))

}
