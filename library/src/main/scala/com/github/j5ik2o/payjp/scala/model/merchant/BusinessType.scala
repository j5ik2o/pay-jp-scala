package com.github.j5ik2o.payjp.scala.model.merchant

import cats.Eq
import enumeratum._
import io.circe.Decoder

import scala.collection.immutable

sealed abstract class BusinessType(override val entryName: String) extends EnumEntry

object BusinessType extends Enum[BusinessType] {

  override def values: immutable.IndexedSeq[BusinessType] = findValues

  case object Company  extends BusinessType("company")
  case object SoleProp extends BusinessType("sole_prop")

  implicit object BusinessTypeEq extends Eq[BusinessType] {
    override def eqv(x: BusinessType, y: BusinessType): Boolean = x == y
  }

  implicit val businessTypeDecoder: Decoder[BusinessType] = Decoder[String].map(BusinessType.withName)

}
