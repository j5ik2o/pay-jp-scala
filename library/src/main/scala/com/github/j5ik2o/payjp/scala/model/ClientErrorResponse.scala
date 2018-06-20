package com.github.j5ik2o.payjp.scala.model

import io.circe.{ Decoder, Encoder, Json }
import io.circe.syntax._

case class ClientErrorResponse(`type`: String,
                               status: Int,
                               message: String,
                               param: Option[String],
                               code: Option[String])

object ClientErrorResponse {

  implicit val ClientErrorResponseDecoder: Decoder[ClientErrorResponse] = Decoder.instance { hcursor =>
    for {
      _type   <- hcursor.get[String]("type")
      status  <- hcursor.get[Int]("status")
      message <- hcursor.get[String]("message")
      param   <- hcursor.get[Option[String]]("param")
      code    <- hcursor.get[Option[String]]("code")
    } yield ClientErrorResponse(_type, status, message, param, code)
  }

  implicit val ClientErrorResponseEncoder: Encoder[ClientErrorResponse] = Encoder.instance { v =>
    Json.obj(
      "type"    -> v.`type`.asJson,
      "status"  -> v.status.asJson,
      "message" -> v.message.asJson,
      "param"   -> v.param.asJson,
      "code"    -> v.code.asJson
    )
  }

}
