package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import cats.Eq
import io.circe.Decoder

case class TokenId(value: String) {
  require(value.startsWith("tok_"))
}

object TokenId extends JsonImplicits {

  implicit object TokenIdEq extends Eq[TokenId] {
    override def eqv(x: TokenId, y: TokenId): Boolean = x == y
  }

  implicit val TokenIdDecoder: Decoder[TokenId] = Decoder.decodeString.map(TokenId(_))

}

case class Token(id: TokenId, livemode: Boolean, created: ZonedDateTime, used: Boolean, card: Card)

object Token extends JsonImplicits {

  implicit object TokenEq extends Eq[Token] {
    override def eqv(x: Token, y: Token): Boolean = x == y
  }

  implicit val TokenDecoder: Decoder[Token] =
    Decoder.forProduct5("id", "livemode", "created", "used", "card")(Token.apply)

}
