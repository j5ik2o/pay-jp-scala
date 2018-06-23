package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import cats.Eq
import io.circe.Decoder

case class CustomerId(value: String) {
  require(value.startsWith("cus_"))
}

object CustomerId {
  implicit object CustomerIdEq extends Eq[CustomerId] {
    override def eqv(x: CustomerId, y: CustomerId): Boolean = x == y
  }
  implicit val CustomerIdDecoder: Decoder[CustomerId] = Decoder.decodeString.map(CustomerId(_))
}

case class Customer(id: CustomerId,
                    livemode: Boolean,
                    email: Option[String],
                    description: Option[String],
                    defaultCardId: Option[String],
                    metadata: Option[Map[String, String]],
                    cards: Collection[Card],
                    created: ZonedDateTime)

object Customer extends JsonImplicits {

  implicit object CustomerEq extends Eq[Customer] {
    override def eqv(x: Customer, y: Customer): Boolean = x == y
  }

  implicit val CustomerDecoder: Decoder[Customer] =
    Decoder.forProduct8("id", "livemode", "email", "description", "default_card", "metadata", "cards", "created")(
      Customer.apply
    )

}
