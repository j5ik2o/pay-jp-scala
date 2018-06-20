package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import io.circe.syntax._
import io.circe.{ Decoder, Encoder, Json }

case class CustomerResponse(id: String,
                            liveMode: Boolean,
                            email: Option[String],
                            description: Option[String],
                            defaultCardId: Option[String],
                            metaData: Map[String, String],
                            cardsRef: CardsRef,
                            created: ZonedDateTime) {
  val `object` = "customer"
}

object CustomerResponse extends JsonImplicits {

  implicit val CustomerEncoder: Encoder[CustomerResponse] = Encoder.instance { v =>
    Json.obj(
      "id"           -> v.id.asJson,
      "livemode"     -> v.liveMode.asJson,
      "email"        -> v.email.asJson,
      "description"  -> v.description.asJson,
      "default_card" -> v.defaultCardId.asJson,
      "metadata"     -> v.metaData.asJson,
      "cards"        -> v.cardsRef.asJson,
      "created"      -> v.created.asJson,
      "object"       -> v.`object`.asJson
    )
  }

  implicit val CustomerDecoder: Decoder[CustomerResponse] = Decoder.instance { hcursor =>
    for {
      id            <- hcursor.get[String]("id")
      liveMode      <- hcursor.get[Boolean]("livemode")
      email         <- hcursor.get[Option[String]]("email")
      description   <- hcursor.get[Option[String]]("description")
      defaultCardId <- hcursor.get[Option[String]]("default_card")
      metadata      <- hcursor.get[Map[String, String]]("metadata")
      cardsRef      <- hcursor.get[CardsRef]("cards")
      created       <- hcursor.get[ZonedDateTime]("created")
    } yield CustomerResponse(id, liveMode, email, description, defaultCardId, metadata, cardsRef, created)
  }

}
