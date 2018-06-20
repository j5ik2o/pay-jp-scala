package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import io.circe.{ Decoder, Encoder, Json }
import io.circe.syntax._

@deprecated(since = "2018/7")
case class Card(id: String,
                brand: String,
                liveMode: Boolean,
                number: String,
                expYear: Int,
                expMonth: Int,
                name: String,
                country: String,
                addressZip: String,
                addressState: String,
                addressCity: String,
                addressLine1: String,
                addressLine2: String,
                cvc: String,
                fingerPrint: String,
                created: ZonedDateTime)

object Card extends JsonImplicits {

  implicit val CardEncoder: Encoder[Card] = Encoder.instance { v =>
    Json.obj(
      "id"            -> v.id.asJson,
      "brand"         -> v.brand.asJson,
      "livemode"      -> v.liveMode.asJson,
      "number"        -> v.number.asJson,
      "expYear"       -> v.expYear.asJson,
      "expMonth"      -> v.expMonth.asJson,
      "name"          -> v.name.asJson,
      "country"       -> v.country.asJson,
      "address_zip"   -> v.addressZip.asJson,
      "address_state" -> v.addressState.asJson,
      "address_city"  -> v.addressCity.asJson,
      "address_line1" -> v.addressLine1.asJson,
      "address_line2" -> v.addressLine2.asJson,
      "cvc"           -> v.cvc.asJson,
      "finger_print"  -> v.fingerPrint.asJson,
      "created"       -> v.created.asJson
    )
  }

  implicit val CardDecoder: Decoder[Card] = Decoder.instance { hcursor =>
    for {
      id           <- hcursor.get[String]("id")
      brand        <- hcursor.get[String]("brand")
      liveMode     <- hcursor.get[Boolean]("livemode")
      number       <- hcursor.get[String]("number")
      expYear      <- hcursor.get[Int]("exp_year")
      expMonth     <- hcursor.get[Int]("exp_month")
      name         <- hcursor.get[String]("name")
      country      <- hcursor.get[String]("country")
      addressZip   <- hcursor.get[String]("address_zip")
      addressState <- hcursor.get[String]("address_state")
      addressCity  <- hcursor.get[String]("address_city")
      addressLine1 <- hcursor.get[String]("address_line1")
      addressLine2 <- hcursor.get[String]("address_line2")
      cvc          <- hcursor.get[String]("cvc")
      fingerPrint  <- hcursor.get[String]("fingerprint")
      created      <- hcursor.get[ZonedDateTime]("created")
    } yield
      Card(id,
           brand,
           liveMode,
           number,
           expYear,
           expMonth,
           name,
           country,
           addressZip,
           addressState,
           addressCity,
           addressLine1,
           addressLine2,
           cvc,
           fingerPrint,
           created)
  }

}
