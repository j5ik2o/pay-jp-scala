package com.github.j5ik2o.payjp.scala.model.merchant

import cats.Eq
import enumeratum._
import io.circe.Decoder

import scala.collection.immutable

sealed abstract class BankAccountType(override val entryName: String) extends EnumEntry {}

object BankAccountType extends Enum[BankAccountType] {

  override def values: immutable.IndexedSeq[BankAccountType] = findValues

  case object Normal  extends BankAccountType("普通")
  case object Current extends BankAccountType("当座 ")

  implicit object BankAccountTypeEq extends Eq[BankAccountType] {
    override def eqv(x: BankAccountType, y: BankAccountType): Boolean = x == y
  }

  implicit val BankAccountTypeDecoder: Decoder[BankAccountType] = Decoder.decodeString.map(BankAccountType.withName)

}
