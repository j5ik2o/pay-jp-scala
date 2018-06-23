package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import cats.Eq
import io.circe.Decoder

case class ProductId(value: String) {
  require(value.startsWith("prd_"))
}

object ProductId {
  implicit object ProductIdEq extends Eq[ProductId] {
    override def eqv(x: ProductId, y: ProductId): Boolean = x == y
  }
  implicit val productIdDecoder: Decoder[ProductId] = Decoder.decodeString.map(ProductId(_))
}

/**
  * プロダクト。
  *
  * 支払いのひな型となるオブジェクト。
  *
  * @see https://pay.jp/docs/api/#product-%E3%83%97%E3%83%AD%E3%83%80%E3%82%AF%E3%83%88
  *
  * @param id
  * @param liveMode
  * @param created
  * @param amount
  * @param currency
  * @param capture
  * @param invalidAfter
  * @param metadata
  * @param payCodeUrl
  */
case class Product(id: ProductId,
                   liveMode: Boolean,
                   created: ZonedDateTime,
                   amount: Amount,
                   currency: Currency,
                   capture: Boolean,
                   invalidAfter: Option[ZonedDateTime],
                   metadata: Option[Map[String, String]],
                   payCodeUrl: String)

object Product extends JsonImplicits {

  implicit object ProductEq extends Eq[Product] {
    override def eqv(x: Product, y: Product): Boolean = x == y
  }

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
