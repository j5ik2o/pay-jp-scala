package com.github.j5ik2o.payjp.scala.model

import enumeratum._
import io.circe.Decoder

import scala.collection.immutable

sealed abstract class TransferStatus(override val entryName: String) extends EnumEntry

object TransferStatus extends Enum[TransferStatus] {
  override def values: immutable.IndexedSeq[TransferStatus] = findValues
  case object Pending     extends TransferStatus("pending")
  case object Paid        extends TransferStatus("paid")
  case object Failed      extends TransferStatus("failed")
  case object Canceled    extends TransferStatus("canceled")
  case object CarriedOver extends TransferStatus("carried_over")

  implicit val TransferStatusDecoder: Decoder[TransferStatus] = Decoder.decodeString.map(TransferStatus.withName)

}
