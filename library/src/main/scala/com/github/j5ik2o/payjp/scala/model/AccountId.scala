package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import io.circe.Decoder

case class AccountId(value: String) {
  require(value.startsWith("acct_"))
}

object AccountId {
  implicit val AccountIdDecoder: Decoder[AccountId] = Decoder.decodeString.map(AccountId(_))
}

case class Account(id: AccountId, email: Option[String], merchant: Account.Merchant, created: ZonedDateTime)

object Account extends JsonImplicits {

  implicit val AccountDecoder: Decoder[Account] =
    Decoder.forProduct4("id", "email", "merchant", "created")(Account.apply)

  case class MerchantId(value: String) {
    require(value.startsWith("acct_mch_"))
  }

  object MerchantId {
    implicit val MerchantIdDecoder: Decoder[MerchantId] = Decoder.decodeString.map(MerchantId(_))
  }

  case class Merchant(id: MerchantId,
                      bankEnabled: Boolean,
                      brandsAccepted: Seq[String],
                      currenciesSupported: Seq[Currency],
                      defaultCurrency: Currency,
                      detailsSubmitted: Boolean,
                      businessType: Option[String],
                      contactPhone: Option[String],
                      country: Option[String],
                      chargeType: Option[Seq[String]],
                      productDetail: Option[String],
                      productName: Option[String],
                      productType: Option[Seq[String]],
                      liveModeEnabled: Boolean,
                      liveModeActivatedAt: Option[ZonedDateTime],
                      sitePublished: Option[Boolean],
                      url: Option[String],
                      created: ZonedDateTime)

  object Merchant extends JsonImplicits {

    implicit val MerchantDecoder: Decoder[Merchant] =
      Decoder.forProduct18(
        "id",
        "bank_enabled",
        "brands_accepted",
        "currencies_supported",
        "default_currency",
        "details_submitted",
        "business_type",
        "contact_phone",
        "country",
        "charge_type",
        "product_detail",
        "product_name",
        "product_type",
        "livemode_enabled",
        "livemode_activated_at",
        "site_published",
        "url",
        "created"
      )(
        Merchant.apply
      )
  }

}
