package com.github.j5ik2o.payjp.scala.model.merchant

import enumeratum._

import scala.collection.immutable

sealed abstract class ProductType(override val entryName: String) extends EnumEntry

object ProductType extends Enum[ProductType] {

  override def values: immutable.IndexedSeq[ProductType] = findValues

  case object Goods    extends ProductType("goods")
  case object Services extends ProductType("services")
  case object Contents extends ProductType("contents")

}
