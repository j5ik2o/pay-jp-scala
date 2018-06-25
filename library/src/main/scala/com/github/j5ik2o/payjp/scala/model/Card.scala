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
  * @param expYear
  * @param expMonth
  * @param nameOpt
  * @param countryOpt
  * @param addressZipOpt
  * @param addressStateOpt
  * @param addressCityOpt
  * @param addressLine1Opt
  * @param addressLine2Opt
  * @param cvcChecked
  * @param fingerPrint
  * @param customerIdOpt
  * @param created
  */
case class Card(id: CardId,
                brand: String,
                livemode: Boolean,
                expYear: Int,
                expMonth: Int,
                nameOpt: Option[String],
                countryOpt: Option[String],
                addressZipOpt: Option[String],
                addressZipCheck: Option[String],
                addressStateOpt: Option[String],
                addressCityOpt: Option[String],
                addressLine1Opt: Option[String],
                addressLine2Opt: Option[String],
                cvcChecked: String,
                fingerPrint: String,
                customerIdOpt: Option[CustomerId],
                created: ZonedDateTime)

object Card extends JsonImplicits {

  implicit object CardEq extends Eq[Card] {
    override def eqv(x: Card, y: Card): Boolean = x == y
  }

  implicit val CardDecoder: Decoder[Card] = Decoder.forProduct17(
    "id",
    "brand",
    "livemode",
    "exp_year",
    "exp_month",
    "name",
    "country",
    "address_zip",
    "address_zip_check",
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
