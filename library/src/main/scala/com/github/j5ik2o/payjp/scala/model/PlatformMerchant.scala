package com.github.j5ik2o.payjp.scala.model

import java.time.{ LocalDate, YearMonth, ZonedDateTime }

import cats.Eq
import com.github.j5ik2o.payjp.scala.model.merchant._
import io.circe.Decoder

case class MerchantKeys(livePublicKey: String,
                        liveSecretKey: SecretKey,
                        testPublicKey: String,
                        testSecretKey: SecretKey)

object MerchantKeys {

  implicit object MerchantKeysEq extends Eq[MerchantKeys] {
    override def eqv(x: MerchantKeys, y: MerchantKeys): Boolean = x == y
  }

  implicit val MerchantKeysDecoder: Decoder[MerchantKeys] =
    Decoder.forProduct4("live_public_key", "live_secret_key", "test_public_key", "test_secret_key")(MerchantKeys.apply)
}

/**
  * 店舗アカウント
  */
case class PlatformMerchant(id: AccountId,
                            displayName: Option[String],
                            merchant: PlatformMerchant.Merchant,
                            keysOpt: Option[MerchantKeys],
                            created: ZonedDateTime)

object PlatformMerchant extends JsonImplicits {

  implicit object PlatformMerchantEq extends Eq[PlatformMerchant] {
    override def eqv(x: PlatformMerchant, y: PlatformMerchant): Boolean = x == y
  }

  implicit val merchantDecoder: Decoder[PlatformMerchant] =
    Decoder.forProduct5("id", "display_name", "merchant", "keys", "created")(PlatformMerchant.apply)

  case class OtherFee(
      deliveryFee: Int,
      deliveryDetail: String,
      otherFee: Int,
      otherFeeDetail: String
  )

  case class Application(address: Address,
                         bank: Bank,
                         businessCapitalOpt: Option[Long],
                         businessDetailOpt: Option[String],
                         businessName: Name,
                         businessSalesLastYearOpt: Option[Int],
                         businessType: BusinessType,
                         cellPhone: String,
                         chargeTypeOpt: Option[ChargeType],
                         contactPersonOpt: Option[String],
                         contactPhoneOpt: Option[String],
                         corporateNumberOpt: Option[String],
                         dateOfEstablishment: LocalDate,
                         hasShopOpt: Option[Boolean],
                         licenseCertFiles: Seq[String],
                         openingBusinessCertFiles: Seq[String],
                         otherFeeOpt: Option[OtherFee],
                         phone: String,
                         presidentBirthAt: LocalDate,
                         presidentFirstName: Name,
                         presidentLastName: Name,
                         presidentGender: String,
                         privacyPolicyUrlOpt: Option[String],
                         productDetail: ProductDetail,
                         productName: ProductName,
                         productPrice: ProductPrice,
                         productTypeOpt: Option[String],
                         sclOpt: Option[Scl],
                         serviceStartAt: YearMonth,
                         shopAddressOpt: Option[Address],
                         shopPhoneOpt: Option[String],
                         sitePublishedOpt: Option[Boolean],
                         sslEnabledOpt: Option[Boolean],
                         termsOfServiceOpt: Option[String],
                         url: String,
                         usingServiceOpt: Option[String])

  object Application extends JsonImplicits {
    implicit val ApplicationDecoder: Decoder[Application] = Decoder.instance { hcursor =>
      for {
        addressZip                 <- hcursor.get[String]("address_zip")
        addressState               <- hcursor.get[String]("address_state")
        addressCity                <- hcursor.get[String]("address_city")
        addressLine1               <- hcursor.get[String]("address_line1")
        addressLine2               <- hcursor.get[String]("address_line2")
        addressReadingState        <- hcursor.get[String]("address_reading_state")
        addressReadingCity         <- hcursor.get[String]("address_reading_city")
        addressReadingLine1        <- hcursor.get[String]("address_reading_line1")
        addressReadingLine2        <- hcursor.get[String]("address_reading_line2")
        bankAccountNumber          <- hcursor.get[String]("bank_account_number")
        bankBranchCode             <- hcursor.get[String]("bank_branch_code")
        bankCode                   <- hcursor.get[String]("bank_code")
        bankPersonName             <- hcursor.get[String]("bank_person_name")
        bankType                   <- hcursor.get[BankAccountType]("bank_type")
        businessCapitalOpt         <- hcursor.get[Option[Long]]("business_capital")
        businessDetailOpt          <- hcursor.get[Option[String]]("business_detail")
        businessName               <- hcursor.get[String]("business_name")
        businessReadingName        <- hcursor.get[String]("business_reading_name")
        businessSalesLastYearOpt   <- hcursor.get[Option[Int]]("business_sales_lastyear")
        businessType               <- hcursor.get[BusinessType]("business_type")
        cellPhone                  <- hcursor.get[String]("cell_phone")
        chargeTypeOpt              <- hcursor.get[Option[ChargeType]]("charge_type")
        contactPersonOpt           <- hcursor.get[Option[String]]("contact_person")
        contactPhoneOpt            <- hcursor.get[Option[String]]("contact_phone")
        corporateNumberOpt         <- hcursor.get[Option[String]]("corporate_number")
        dateOfEstablishment        <- hcursor.get[LocalDate]("date_of_establishment")
        deliveryDetailOpt          <- hcursor.get[Option[String]]("delivery_detail")
        deliveryFeeOpt             <- hcursor.get[Option[Int]]("delivery_fee")
        hasShopOpt                 <- hcursor.get[Option[Boolean]]("has_shop")
        licenseCertFiles           <- hcursor.get[Seq[String]]("license_cert_files")
        openingBusinessCertFiles   <- hcursor.get[Seq[String]]("opening_business_cert_files")
        otherFeeOpt                <- hcursor.get[Option[Int]]("other_fee")
        otherFeeDetailOpt          <- hcursor.get[Option[String]]("other_fee_detail")
        phone                      <- hcursor.get[String]("phone")
        presidentBirthAt           <- hcursor.get[LocalDate]("president_birth_at")
        presidentFirstName         <- hcursor.get[String]("president_firstname")
        presidentLastName          <- hcursor.get[String]("president_lastname")
        presidentReadingFirstName  <- hcursor.get[String]("president_reading_firstname")
        presidentReadingLastName   <- hcursor.get[String]("president_reading_lastname")
        presidentGender            <- hcursor.get[String]("president_gender")
        privacyPolicyUrlOpt        <- hcursor.get[Option[String]]("privacy_policy_urls")
        productDetail              <- hcursor.get[String]("product_detail")
        productDetailDocument      <- hcursor.get[Seq[String]]("product_detail_document")
        productName                <- hcursor.get[String]("product_name")
        productEnglishName         <- hcursor.get[String]("product_english_name")
        productReadingName         <- hcursor.get[String]("product_reading_name")
        productPriceMin            <- hcursor.get[Int]("product_price_min")
        productPriceMax            <- hcursor.get[Int]("product_price_min")
        productTypeOpt             <- hcursor.get[Option[String]]("product_type")
        sclAddressOpt              <- hcursor.get[Option[String]]("scl_address")
        sclBusinessNameOpt         <- hcursor.get[Option[String]]("scl_business_name")
        sclContactPersonNameOpt    <- hcursor.get[Option[String]]("scl_contact_person_name")
        sclEmailOpt                <- hcursor.get[Option[String]]("scl_email")
        sclOtherFeeOpt             <- hcursor.get[Option[Int]]("scl_other_fee")
        sclPaymentMethodOpt        <- hcursor.get[Option[String]]("scl_payment_method")
        sclPhoneOpt                <- hcursor.get[Option[String]]("scl_phone")
        sclReturnAndExchangeOpt    <- hcursor.get[Option[String]]("scl_return_and_exchange")
        sclTimeOfDeliveryOpt       <- hcursor.get[Option[String]]("scl_time_of_delivery")
        sclTimeOfPaymentOpt        <- hcursor.get[Option[String]]("scl_time_of_payment")
        sclUrlOpt                  <- hcursor.get[Option[String]]("scl_url")
        serviceStartAt             <- hcursor.get[YearMonth]("service_start_at")
        shopAddressCityOpt         <- hcursor.get[Option[String]]("shop_address_city")
        shopAddressLine1Opt        <- hcursor.get[Option[String]]("shop_address_line1")
        shopAddressLine2Opt        <- hcursor.get[Option[String]]("shop_address_line2")
        shopAddressReadingCityOpt  <- hcursor.get[Option[String]]("shop_address_reading_city")
        shopAddressReadingLine1Opt <- hcursor.get[Option[String]]("shop_address_reading_line1")
        shopAddressReadingLine2Opt <- hcursor.get[Option[String]]("shop_address_reading_line2")
        shopAddressReadingStateOpt <- hcursor.get[Option[String]]("shop_address_reading_state")
        shopAddressStateOpt        <- hcursor.get[Option[String]]("shop_address_state")
        shopAddressZipOpt          <- hcursor.get[Option[String]]("shop_address_zip")
        shopPhoneOpt               <- hcursor.get[Option[String]]("shop_phone")
        sitePublishedOpt           <- hcursor.get[Option[Boolean]]("site_published")
        sslEnabledOpt              <- hcursor.get[Option[Boolean]]("ssl_enabled")
        termsOfServiceOpt          <- hcursor.get[Option[String]]("terms_of_service")
        url                        <- hcursor.get[String]("url")
        usingService               <- hcursor.get[Option[String]]("using_service")
      } yield
        Application(
          address = Address(
            zip = addressZip,
            state = Name(addressState, addressReadingState),
            city = Name(addressCity, addressReadingCity),
            line1 = Name(addressLine1, addressReadingLine1),
            line2 = Name(addressLine2, addressReadingLine2)
          ),
          bank = Bank(
            code = bankCode,
            branchCode = bankBranchCode,
            `type` = bankType,
            accountNumber = bankAccountNumber,
            personName = bankPersonName
          ),
          businessCapitalOpt,
          businessDetailOpt,
          businessName = Name(businessName, businessReadingName),
          businessSalesLastYearOpt,
          businessType,
          cellPhone,
          chargeTypeOpt,
          contactPersonOpt,
          contactPhoneOpt,
          corporateNumberOpt,
          dateOfEstablishment,
          hasShopOpt,
          licenseCertFiles,
          openingBusinessCertFiles, {
            (deliveryFeeOpt, deliveryDetailOpt, otherFeeOpt, otherFeeDetailOpt) match {
              case (Some(deliveryFee), Some(deliveryDetail), Some(otherFee), Some(otherFeeDetail)) =>
                Some(OtherFee(deliveryFee, deliveryDetail, otherFee, otherFeeDetail))
              case _ => None
            }
          },
          phone,
          presidentBirthAt,
          presidentFirstName = Name(presidentFirstName, presidentReadingFirstName),
          presidentLastName = Name(presidentLastName, presidentReadingLastName),
          presidentGender,
          privacyPolicyUrlOpt,
          productDetail = ProductDetail(productDetail, productDetailDocument),
          productName = ProductName(productName, productReadingName, productEnglishName),
          productPrice = ProductPrice(productPriceMin, productPriceMax),
          productTypeOpt,
          sclOpt = {
            (sclAddressOpt,
             sclBusinessNameOpt,
             sclContactPersonNameOpt,
             sclEmailOpt,
             sclOtherFeeOpt,
             sclPaymentMethodOpt,
             sclPhoneOpt,
             sclReturnAndExchangeOpt,
             sclTimeOfDeliveryOpt,
             sclTimeOfPaymentOpt,
             sclUrlOpt) match {
              case (Some(sclAddress),
                    Some(sclBusinessName),
                    Some(sclContactPersonName),
                    Some(sclEmail),
                    Some(sclOtherFee),
                    Some(sclPaymentMethod),
                    Some(sclPhone),
                    Some(sclReturnAndExchange),
                    Some(sclTimeOfDelivery),
                    Some(sclTimeOfPayment),
                    Some(sclUrl)) =>
                Some(
                  Scl(
                    sclBusinessName,
                    sclContactPersonName,
                    sclEmail,
                    sclPhone,
                    sclAddress,
                    sclOtherFee,
                    sclPaymentMethod,
                    sclTimeOfPayment,
                    sclTimeOfDelivery,
                    sclReturnAndExchange,
                    sclUrl
                  )
                )
              case _ =>
                None
            }
          },
          serviceStartAt,
          shopAddressOpt = {
            (shopAddressZipOpt,
             shopAddressStateOpt,
             shopAddressCityOpt,
             shopAddressLine1Opt,
             shopAddressLine2Opt,
             shopAddressReadingStateOpt,
             shopAddressReadingCityOpt,
             shopAddressReadingLine1Opt,
             shopAddressReadingLine2Opt) match {
              case (Some(shopZip),
                    Some(shopState),
                    Some(shopCity),
                    Some(shopLine1),
                    Some(shopLine2),
                    Some(shopReadingState),
                    Some(shopReadingCity),
                    Some(shopReadingLine1),
                    Some(shopReadingLine2)) =>
                Some(
                  Address(
                    zip = shopZip,
                    state = Name(shopState, shopReadingState),
                    city = Name(shopCity, shopReadingCity),
                    line1 = Name(shopLine1, shopReadingLine1),
                    line2 = Name(shopLine2, shopReadingLine2)
                  )
                )
              case _ =>
                None
            }
          },
          shopPhoneOpt,
          sitePublishedOpt,
          sslEnabledOpt,
          termsOfServiceOpt,
          url,
          usingService
        )
    }
  }

  case class Merchant(applicationOpt: Option[Application],
                      bankEnabled: Boolean,
                      brandsAccepted: Seq[String],
                      currenciesSupported: Seq[Currency],
                      defaultCurrency: Currency,
                      countryOpt: Option[String],
                      liveModeEnabled: Boolean,
                      liveModeActivatedAt: Option[ZonedDateTime],
                      created: ZonedDateTime)

  object Merchant extends JsonImplicits {

    implicit object MerchantEq extends Eq[Merchant] {
      override def eqv(x: Merchant, y: Merchant): Boolean = x == y
    }

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
