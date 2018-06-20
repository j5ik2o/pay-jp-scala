package com.github.j5ik2o.payjp.scala.model

import io.circe.{ Decoder, Encoder, Json }
import io.circe.syntax._

case class CardsRef(count: Int, data: Seq[Card], hasMore: Boolean, url: String)

object CardsRef {

  implicit val CardsRefEncoder: Encoder[CardsRef] = Encoder.instance { v =>
    Json.obj(
      "count"    -> v.count.asJson,
      "data"     -> v.data.asJson,
      "has_more" -> v.hasMore.asJson,
      "url"      -> v.url.asJson
    )
  }

  implicit val CardsRefDecoder: Decoder[CardsRef] = Decoder.instance { hcursor =>
    for {
      count   <- hcursor.get[Int]("count")
      data    <- hcursor.get[Seq[Card]]("data")
      hasMore <- hcursor.get[Boolean]("has_more")
      url     <- hcursor.get[String]("url")
    } yield CardsRef(count, data, hasMore, url)
  }
}
