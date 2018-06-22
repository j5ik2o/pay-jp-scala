package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import io.circe.Decoder

case class TokenId(value: String) {
  require(value.startsWith("tok_"))
}

object TokenId extends JsonImplicits {

  implicit val TokenIdDecoder: Decoder[TokenId] = Decoder.decodeString.map(TokenId(_))

}

case class Token(id: TokenId, livemode: String, created: ZonedDateTime, used: Boolean, card: Card)

