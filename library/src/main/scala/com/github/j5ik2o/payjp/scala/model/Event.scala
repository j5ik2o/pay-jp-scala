package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import cats.Eq
import io.circe.{ Decoder, Json }

case class EventId(value: String) {
  require(value.startsWith("evnt_"))
}

object EventId {
  implicit object EventIdEq extends Eq[EventId] {
    override def eqv(x: EventId, y: EventId): Boolean = x == y
  }
  implicit val EventIdDecoder: Decoder[EventId] = Decoder.decodeString.map(EventId(_))
}

case class Event(id: EventId,
                 livemode: Boolean,
                 created: ZonedDateTime,
                 `type`: String,
                 pendingWebhooks: Int,
                 data: Json)

object Event extends JsonImplicits {

  implicit object EventEq extends Eq[Event] {
    override def eqv(x: Event, y: Event): Boolean = x == y
  }

  implicit val EventDecoder: Decoder[Event] =
    Decoder.forProduct6("id", "livemode", "created", "type", "pending_webhooks", "data")(Event.apply)

}
