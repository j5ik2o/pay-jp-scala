package com.github.j5ik2o.payjp.scala.model.merchant

import cats.Eq
import enumeratum._

import scala.collection.immutable

sealed abstract class GenderType(override val entryName: String) extends EnumEntry

object GenderType extends Enum[GenderType] {
  override def values: immutable.IndexedSeq[GenderType] = findValues

  case object Men   extends GenderType("men")
  case object Woman extends GenderType("woman")

  implicit object GenderTypeEq extends Eq[GenderType] {
    override def eqv(x: GenderType, y: GenderType): Boolean = x == y
  }

}
