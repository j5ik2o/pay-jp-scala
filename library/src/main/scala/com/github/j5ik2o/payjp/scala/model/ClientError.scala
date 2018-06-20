package com.github.j5ik2o.payjp.scala.model

import io.circe.Decoder

case class ClientError(`type`: String, status: Int, message: String, param: Option[String], code: Option[String])

object ClientError {

  implicit val ClientErrorResponseDecoder: Decoder[ClientError] = Decoder.instance { hcursor =>
    for {
      _type   <- hcursor.get[String]("type")
      status  <- hcursor.get[Int]("status")
      message <- hcursor.get[String]("message")
      param   <- hcursor.get[Option[String]]("param")
      code    <- hcursor.get[Option[String]]("code")
    } yield ClientError(_type, status, message, param, code)
  }

}
