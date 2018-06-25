package com.github.j5ik2o.payjp.scala.model

import cats.Eq
import enumeratum._

import scala.collection.immutable

sealed abstract class TimingType(override val entryName: String) extends EnumEntry

object TimingType extends Enum[TimingType] {
  override def values: immutable.IndexedSeq[TimingType] = findValues
  case object Now          extends TimingType("now")
  case object After24Hours extends TimingType("24hours")

  implicit object TimingTypeEq extends Eq[TimingType] {
    override def eqv(x: TimingType, y: TimingType): Boolean = x == y
  }

}
