package com.github.j5ik2o.payjp.scala.model

import cats.Eq
import enumeratum._

import scala.collection.immutable

sealed abstract class AccessModeType(override val entryName: String) extends EnumEntry

object AccessModeType extends Enum[AccessModeType] {
  override def values: immutable.IndexedSeq[AccessModeType] = findValues

  case object LiveMode extends AccessModeType("livemode")
  case object TestMode extends AccessModeType("testmode")

  implicit object AccessModeTypeEq extends Eq[AccessModeType] {
    override def eqv(x: AccessModeType, y: AccessModeType): Boolean = x == y
  }

}
