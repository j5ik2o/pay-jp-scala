package com.github.j5ik2o.payjp.scala.model

import java.time.format.DateTimeFormatter
import java.time.{ Instant, LocalDate, ZoneId, ZonedDateTime }

import cats.Eq
import io.circe.{ Decoder, Encoder }

trait JsonImplicits {

  implicit val fromStringToLocalDateDencoder: Decoder[LocalDate] = Decoder.decodeString.map { v =>
    LocalDate.parse(v, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
  }

  implicit val ZonedDateTimeEncoder: Encoder[ZonedDateTime] =
    Encoder[Long].contramap(_.toInstant.getEpochSecond)

  implicit val ZonedDateTimeDecoder: Decoder[ZonedDateTime] =
    Decoder[Long].map { v =>
      ZonedDateTime.ofInstant(Instant.ofEpochSecond(v), ZoneId.systemDefault())
    }

}

trait TypeclassImplicits {

  implicit val ZonedDateTimeEq = new Eq[ZonedDateTime] {
    override def eqv(x: ZonedDateTime, y: ZonedDateTime): Boolean = x == y
  }

  implicit val LocalDataEq = new Eq[LocalDate] {
    override def eqv(x: LocalDate, y: LocalDate): Boolean = x == y
  }

}
