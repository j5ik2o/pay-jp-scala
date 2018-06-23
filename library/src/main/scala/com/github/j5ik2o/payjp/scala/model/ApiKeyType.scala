package com.github.j5ik2o.payjp.scala.model

import enumeratum._

import scala.collection.immutable

sealed abstract class ApiKeyType(override val entryName: String) extends EnumEntry

object ApiKeyType extends Enum[ApiKeyType] {
  override def values: immutable.IndexedSeq[ApiKeyType] = findValues
  case object Public extends ApiKeyType("public")
  case object Secret extends ApiKeyType("secret")
}
