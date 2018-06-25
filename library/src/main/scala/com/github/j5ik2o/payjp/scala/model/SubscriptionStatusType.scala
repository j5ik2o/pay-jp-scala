package com.github.j5ik2o.payjp.scala.model

import cats.Eq
import enumeratum._
import io.circe.Decoder

import scala.collection.immutable

sealed abstract class SubscriptionStatusType(override val entryName: String) extends EnumEntry

object SubscriptionStatusType extends Enum[SubscriptionStatusType] {
  override def values: immutable.IndexedSeq[SubscriptionStatusType] = findValues
  case object Active   extends SubscriptionStatusType("active")
  case object Paused   extends SubscriptionStatusType("paused")
  case object Canceled extends SubscriptionStatusType("canceled")

  implicit object SubscriptionStatusEq extends Eq[SubscriptionStatusType] {
    override def eqv(x: SubscriptionStatusType, y: SubscriptionStatusType): Boolean = x == y
  }

  implicit val subscriptionStatusDecoder: Decoder[SubscriptionStatusType] =
    Decoder[String].map(SubscriptionStatusType.withName)
}
