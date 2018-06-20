package com.github.j5ik2o.payjp.scala.model

import enumeratum._

import scala.collection.immutable

sealed abstract class PlanInterval(override val entryName: String) extends EnumEntry

object PlanInterval extends Enum[PlanInterval] {
  override def values: immutable.IndexedSeq[PlanInterval] = findValues
  case object Month extends PlanInterval("month")
  case object Year  extends PlanInterval("year")
}
