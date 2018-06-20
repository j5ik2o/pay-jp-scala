package com.github.j5ik2o.payjp.scala.model

import io.circe.Decoder

case class AuthError(`type`: String, status: Int, message: String)

object AuthError {

  implicit val AuthErrorResponseDecoder: Decoder[AuthError] = Decoder.instance { hcursor =>
    for {
      _type   <- hcursor.get[String]("type")
      status  <- hcursor.get[Int]("status")
      message <- hcursor.get[String]("message")
    } yield AuthError(_type, status, message)
  }

}
