package com.github.j5ik2o.payjp.scala.model

import cats.Eq
import enumeratum._
import io.circe.Decoder

import scala.collection.immutable

sealed abstract class TransferStatusType(override val entryName: String) extends EnumEntry

object TransferStatusType extends Enum[TransferStatusType] {
  override def values: immutable.IndexedSeq[TransferStatusType] = findValues

  case object Pending     extends TransferStatusType("pending")
  case object Paid        extends TransferStatusType("paid")
  case object Failed      extends TransferStatusType("failed")
  case object Canceled    extends TransferStatusType("canceled")
  case object CarriedOver extends TransferStatusType("carried_over")

  implicit object TransferStatusEq extends Eq[TransferStatusType] {
    override def eqv(x: TransferStatusType, y: TransferStatusType): Boolean = x == y
  }

  implicit val TransferStatusDecoder: Decoder[TransferStatusType] =
    Decoder[String].map(TransferStatusType.withName)

}
