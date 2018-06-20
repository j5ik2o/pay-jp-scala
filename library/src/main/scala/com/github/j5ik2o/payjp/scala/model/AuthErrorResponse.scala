package com.github.j5ik2o.payjp.scala.model

import io.circe.{ Decoder, Encoder, Json }
import io.circe.syntax._

case class AuthErrorResponse(`type`: String, status: Int, message: String)

object AuthErrorResponse {

  implicit val AuthErrorResponseDecoder: Decoder[AuthErrorResponse] = Decoder.instance { hcursor =>
    for {
      _type   <- hcursor.get[String]("type")
      status  <- hcursor.get[Int]("status")
      message <- hcursor.get[String]("message")
    } yield AuthErrorResponse(_type, status, message)
  }

  implicit val AuthErrorResponseEncoder: Encoder[AuthErrorResponse] = Encoder.instance { v =>
    Json.obj(
      "type"    -> v.`type`.asJson,
      "status"  -> v.status.asJson,
      "message" -> v.message.asJson
    )
  }

}
