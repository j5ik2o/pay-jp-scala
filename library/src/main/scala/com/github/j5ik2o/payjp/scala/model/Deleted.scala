package com.github.j5ik2o.payjp.scala.model

import cats.Eq
import io.circe.Decoder
import shapeless.ops.zipper.Delete

case class Deleted[A](id: A, deleted: Boolean, liveMode: Boolean)

object Deleted {

  implicit def DeletedEq[A] = new Eq[Delete[A]] {
    override def eqv(x: Delete[A], y: Delete[A]): Boolean = x == y
  }

  implicit def DeleteResponseDecoder[A: Decoder]: Decoder[Deleted[A]] =
    Decoder.forProduct3("id", "deleted", "livemode")(Deleted.apply)

}
