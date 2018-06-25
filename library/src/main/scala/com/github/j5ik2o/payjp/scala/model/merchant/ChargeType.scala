package com.github.j5ik2o.payjp.scala.model.merchant

import cats.Eq
import enumeratum._
import io.circe.Decoder

import scala.collection.immutable

sealed abstract class ChargeType(override val entryName: String) extends EnumEntry

object ChargeType extends Enum[ChargeType] {
  override def values: immutable.IndexedSeq[ChargeType] = findValues
  case object Charge       extends ChargeType("charge")
  case object Subscription extends ChargeType("subscription")

  implicit object ChargeTypeEq extends Eq[ChargeType] {
    override def eqv(x: ChargeType, y: ChargeType): Boolean = x == y
  }

  implicit val ChargeTypeDecoder: Decoder[ChargeType] = Decoder.decodeString.map(ChargeType.withName)
}
