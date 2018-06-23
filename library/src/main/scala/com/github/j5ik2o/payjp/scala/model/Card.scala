package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import cats.Eq
import io.circe.Decoder

case class CardId(value: String) {
  require(value.startsWith("car_"))
}

object CardId {
  implicit val CustomerCardIdDecoder: Decoder[CardId] = Decoder.decodeString.map(CardId(_))
}

/**
  * カード。
  *
  * @param id
  * @param brand
  * @param livemode
  * @param number
  * @param expYear
  * @param expMonth
  * @param name
  * @param country
  * @param addressZip
  * @param addressState
  * @param addressCity
  * @param addressLine1
  * @param addressLine2
  * @param cvcChecked
  * @param fingerPrint
  * @param customerId
  * @param created
  */
case class Card(id: CardId,
                brand: String,
                livemode: Boolean,
                number: Option[String],
                expYear: Int,
                expMonth: Int,
                name: Option[String],
                country: Option[String],
                addressZip: Option[String],
                addressState: Option[String],
                addressCity: Option[String],
                addressLine1: Option[String],
                addressLine2: Option[String],
                cvcChecked: String,
                fingerPrint: String,
                customerId: Option[CustomerId],
                created: ZonedDateTime)

object Card extends JsonImplicits {

  implicit object CardEq extends Eq[Card] {
    override def eqv(x: Card, y: Card): Boolean = x == y
  }

  implicit val CardDecoder: Decoder[Card] = Decoder.forProduct17(
    "id",
    "brand",
    "livemode",
    "number",
    "exp_year",
    "exp_month",
    "name",
    "country",
    "address_zip",
    "address_state",
    "address_city",
    "address_line1",
    "address_line2",
    "cvc_check",
    "fingerprint",
    "customer",
    "created"
  )(Card.apply)

}
