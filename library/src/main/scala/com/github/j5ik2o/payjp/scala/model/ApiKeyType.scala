package com.github.j5ik2o.payjp.scala.model

import cats.Eq
import enumeratum._
import io.circe.Decoder

import scala.collection.immutable

sealed abstract class ApiKeyType(override val entryName: String) extends EnumEntry

object ApiKeyType extends Enum[ApiKeyType] {
  override def values: immutable.IndexedSeq[ApiKeyType] = findValues
  case object Public extends ApiKeyType("public")
  case object Secret extends ApiKeyType("secret")

  implicit object ApiKeyTypeEq extends Eq[ApiKeyType] {
    override def eqv(x: ApiKeyType, y: ApiKeyType): Boolean = x == y
  }

  implicit val ApiKeyTypeDecoder: Decoder[ApiKeyType] = Decoder[String].map(ApiKeyType.withName)

}
