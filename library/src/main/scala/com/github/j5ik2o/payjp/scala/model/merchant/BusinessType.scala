package com.github.j5ik2o.payjp.scala.model.merchant

import enumeratum._
import io.circe.Decoder

import scala.collection.immutable

sealed abstract class BusinessType(override val entryName: String) extends EnumEntry

object BusinessType extends Enum[BusinessType] {
  override def values: immutable.IndexedSeq[BusinessType] = findValues
  case object Company extends BusinessType("company")

  implicit val businessTypeDecoder: Decoder[BusinessType] = Decoder.decodeString.map(BusinessType.withName)
}
