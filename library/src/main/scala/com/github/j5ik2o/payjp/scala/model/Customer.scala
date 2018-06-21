package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import io.circe.Decoder

case class CustomerId(value: String) {
  require(value.startsWith("cus_"))
}

object CustomerId {
  implicit val CustomerIdDecoder: Decoder[CustomerId] = Decoder.decodeString.map(CustomerId(_))
}

case class Customer(id: CustomerId,
                    liveMode: Boolean,
                    email: Option[String],
                    description: Option[String],
                    defaultCardId: Option[String],
                    metadata: Map[String, String],
                    cards: Collection[Card],
                    created: ZonedDateTime) {
  val `object` = "customer"
}

object Customer extends JsonImplicits {

  implicit val CustomerDecoder: Decoder[Customer] =
    Decoder.forProduct8("id", "livemode", "email", "description", "default_card", "metadata", "cards", "created")(
      Customer.apply
    )

}
