package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import cats.Eq
import io.circe.Decoder

case class AccountId(value: String) {
  require(value.startsWith("acct_"))
}

object AccountId {

  implicit object AccountIdEq extends Eq[AccountId] {
    override def eqv(x: AccountId, y: AccountId): Boolean = x == y
  }

  implicit val AccountIdDecoder: Decoder[AccountId] = Decoder.decodeString.map(AccountId(_))
}

case class Account(id: AccountId, emailOpt: Option[String], merchant: Account.Merchant, created: ZonedDateTime)

object Account extends JsonImplicits {

  implicit object AccountEq extends Eq[Account] {
    override def eqv(x: Account, y: Account): Boolean = x == y
  }

  implicit val AccountDecoder: Decoder[Account] =
    Decoder.forProduct4("id", "email", "merchant", "created")(Account.apply)

  case class MerchantId(value: String) {
    require(value.startsWith("acct_mch_"))
  }

  object MerchantId {
    implicit object MerchantIdEq extends Eq[Merchant] {
      override def eqv(x: Merchant, y: Merchant): Boolean = x == y
    }
    implicit val MerchantIdDecoder: Decoder[MerchantId] = Decoder.decodeString.map(MerchantId(_))
  }

  case class Merchant(id: MerchantId,
                      bankEnabled: Boolean,
                      brandsAccepted: Seq[String],
                      currenciesSupported: Seq[Currency],
                      defaultCurrency: Currency,
                      detailsSubmitted: Boolean,
                      businessTypeOpt: Option[String],
                      contactPhoneOpt: Option[String],
                      countryOpt: Option[String],
                      chargeTypesOpt: Option[Seq[String]],
                      productDetailOpt: Option[String],
                      productNameOpt: Option[String],
                      productTypesOpt: Option[Seq[String]],
                      liveModeEnabled: Boolean,
                      liveModeActivatedAtOpt: Option[ZonedDateTime],
                      sitePublishedOpt: Option[Boolean],
                      urlOpt: Option[String],
                      created: ZonedDateTime)

  object Merchant extends JsonImplicits {

    implicit object MerchantEq extends Eq[Merchant] {
      override def eqv(x: Merchant, y: Merchant): Boolean = x == y
    }

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
