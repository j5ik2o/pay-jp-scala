package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import io.circe.Decoder

case class MerchantKeys(livePublicKey: String,
                        liveSecretKey: SecretKey,
                        testPublicKey: String,
                        testSecretKey: SecretKey)

object MerchantKeys {
  implicit val MerchantKeysDecoder: Decoder[MerchantKeys] =
    Decoder.forProduct4("live_public_key", "live_secret_key", "test_public_key", "test_secret_key")(MerchantKeys.apply)
}

/**
  * 店舗アカウント
  */
case class PlatformMerchant(id: AccountId,
                            displayName: String,
                            merchant: PlatformMerchant.Merchant,
                            keys: MerchantKeys,
                            created: ZonedDateTime)

object PlatformMerchant extends JsonImplicits {
  implicit val merchantDecoder: Decoder[PlatformMerchant] =
    Decoder.forProduct5("id", "display_name", "merchant", "keys", "created")(PlatformMerchant.apply)

  case class Merchant(application: Option[Map[String, String]],
                      bankEnabled: Boolean,
                      brandsAccepted: Seq[String],
                      currenciesSupported: Seq[Currency],
                      defaultCurrency: Currency,
                      country: Option[String],
                      liveModeEnabled: Boolean,
                      liveModeActivatedAt: Option[ZonedDateTime],
                      created: ZonedDateTime)

  object Merchant extends JsonImplicits {

    implicit val MerchantDecoder: Decoder[Merchant] =
      Decoder.forProduct9(
        "application",
        "bank_enabled",
        "brands_accepted",
        "currencies_supported",
        "default_currency",
        "country",
        "livemode_enabled",
        "livemode_activated_at",
        "created"
      )(
        Merchant.apply
      )
  }
}
