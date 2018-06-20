package com.github.j5ik2o.payjp.scala.model

import java.time.{ Instant, ZoneId, ZonedDateTime }

import io.circe.{ Decoder, Encoder, KeyEncoder }

trait JsonImplicits {

  implicit val ZonedDateTimeEncoder: Encoder[ZonedDateTime] =
    Encoder[Long].contramap(_.toInstant.getEpochSecond)

  implicit val ZonedDateTimeDecoder: Decoder[ZonedDateTime] =
    Decoder[Long].map { v =>
      ZonedDateTime.ofInstant(Instant.ofEpochSecond(v), ZoneId.systemDefault())
    }

}
