package com.github.j5ik2o.payjp.scala.model

import io.circe.Decoder

case class SecretKey(value: String)

object SecretKey {
  implicit val SecretKeyDecoder: Decoder[SecretKey] = Decoder.decodeString.map(SecretKey(_))
}
