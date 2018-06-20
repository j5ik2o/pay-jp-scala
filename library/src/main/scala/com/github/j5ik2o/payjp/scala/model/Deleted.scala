package com.github.j5ik2o.payjp.scala.model

import io.circe.Decoder

case class Deleted[A](id: A, deleted: Boolean, liveMode: Boolean)

object Deleted {

  implicit def DeleteResponseDecoder[A: Decoder]: Decoder[Deleted[A]] =
    Decoder.forProduct3("id", "deleted", "livemode")(Deleted.apply)

}
