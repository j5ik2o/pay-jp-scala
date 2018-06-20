package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import io.circe.Decoder

case class CardId(value: String) {
  require(value.startsWith("car_"))
}

object CardId {
  implicit val CustomerCardIdDecoder: Decoder[CardId] = Decoder.decodeString.map(CardId(_))
}

case class Card(id: CardId,
                brand: String,
                liveMode: Boolean,
                number: String,
                expYear: Int,
                expMonth: Int,
                name: Option[String],
                country: Option[String],
                addressZip: String,
                addressState: String,
                addressCity: Option[String],
                addressLine1: Option[String],
                addressLine2: Option[String],
                cvc: String,
                fingerPrint: String,
                customerId: Option[CustomerId],
                created: ZonedDateTime)

object Card extends JsonImplicits {

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
    "cvc",
    "fingerprint",
    "customer",
    "created"
  )(Card.apply)

}
