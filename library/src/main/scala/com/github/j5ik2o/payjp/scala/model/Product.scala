package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import io.circe.Decoder

case class ProductId(value: String) {
  require(value.startsWith("prd_"))
}

object ProductId {
  implicit val productIdDecoder: Decoder[ProductId] = Decoder.decodeString.map(ProductId(_))
}

case class Product(id: ProductId,
                   liveMode: Boolean,
                   created: ZonedDateTime,
                   amount: Amount,
                   currency: Currency,
                   capture: Boolean,
                   invalidAfter: Option[ZonedDateTime],
                   metadata: Map[String, String],
                   payCodeUrl: String)

object Product extends JsonImplicits {
  implicit val ProductDecoder: Decoder[Product] =
    Decoder.forProduct9("id",
                        "livemode",
                        "created",
                        "amount",
                        "currency",
                        "capture",
                        "invalidAfter",
                        "metadata",
                        "paycode_url")(
      Product.apply
    )
}
