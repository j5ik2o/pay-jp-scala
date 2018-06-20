package com.github.j5ik2o.payjp.scala.model

import io.circe.Decoder

case class Collection[A](count: Int, data: Seq[A], hasMore: Boolean, url: String)

object Collection {

  implicit def CardsRefDecoder[A: Decoder]: Decoder[Collection[A]] =
    Decoder.forProduct4("count", "data", "has_more", "url")(Collection.apply)

}
