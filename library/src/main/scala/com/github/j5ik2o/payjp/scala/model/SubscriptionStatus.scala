package com.github.j5ik2o.payjp.scala.model

import enumeratum._
import io.circe.Decoder

import scala.collection.immutable

sealed abstract class SubscriptionStatus(override val entryName: String) extends EnumEntry

object SubscriptionStatus extends Enum[SubscriptionStatus] {
  override def values: immutable.IndexedSeq[SubscriptionStatus] = findValues
  case object Active   extends SubscriptionStatus("active")
  case object Paused   extends SubscriptionStatus("paused")
  case object Canceled extends SubscriptionStatus("canceled")

  implicit val subscriptionStatusDecoder: Decoder[SubscriptionStatus] =
    Decoder.decodeString.map(SubscriptionStatus.withName)
}
