package com.github.j5ik2o.payjp.scala.model

import cats.Eq
import enumeratum._
import io.circe.Decoder

import scala.collection.immutable

sealed abstract class PlanIntervalType(override val entryName: String) extends EnumEntry

object PlanIntervalType extends Enum[PlanIntervalType] {
  override def values: immutable.IndexedSeq[PlanIntervalType] = findValues

  case object Month extends PlanIntervalType("month")
  case object Year  extends PlanIntervalType("year")

  implicit object PlanIntervalEq extends Eq[PlanIntervalType] {
    override def eqv(x: PlanIntervalType, y: PlanIntervalType): Boolean = x == y
  }

  implicit val PlanIntervalDecoder: Decoder[PlanIntervalType] = Decoder[String].map(PlanIntervalType.withName)
}
