package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

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
                            merchant: PlatformMerchant.Merchant,
                            keys: Option[MerchantKeys],
                            created: ZonedDateTime)

object PlatformMerchant extends JsonImplicits {

  implicit object PlatformMerchantEq extends Eq[PlatformMerchant] {
    override def eqv(x: PlatformMerchant, y: PlatformMerchant): Boolean = x == y
  }

  implicit val merchantDecoder: Decoder[PlatformMerchant] =
    Decoder.forProduct4("id", "merchant", "keys", "created")(PlatformMerchant.apply)

  case class OtherFee(
      deliveryFee: Int,
      deliveryDetail: String,
      otherFee: Int,
      otherFeeDetail: String
  )

  case class Application(address: Address,
                         bank: Bank,
                         businessCapital: Option[Long],
                         businessDetailOpt: Option[String],
                         businessName: Name,
                         businessSalesLastYear: Option[Int],
                         businessType: BusinessType,
                         cellPhone: String,
                         chargeTypeOpt: Option[ChargeType],
                         contactPersonOpt: Option[String],
                         contactPhoneOpt: Option[String],
                         corporateNumberOpt: Option[String],
                         dateOfEstablishment: String,
                         hasShopOpt: Option[Boolean],
                         licenseCertFiles: Seq[String],
                         openingBusinessCertFiles: Seq[String],
                         otherFee: Option[OtherFee],
                         phone: String,
                         presidentBirthAt: String,
                         presidentFirstName: Name,
                         presidentLastName: Name,
                         presidentGender: String,
                         privacyPolicyUrlOpt: Option[String],
                         productDetail: ProductDetail,
                         productName: ProductName,
                         productPrice: ProductPrice,
                         productTypeOpt: Option[String],
                         sclOpt: Option[Scl],
                         serviceStartAt: String,
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
        addressZip                <- hcursor.get[String]("address_zip")
        addressState              <- hcursor.get[String]("address_state")
        addressCity               <- hcursor.get[String]("address_city")
        addressLine1              <- hcursor.get[String]("address_line1")
        addressLine2              <- hcursor.get[String]("address_line2")
        addressReadingState       <- hcursor.get[String]("address_reading_state")
        addressReadingCity        <- hcursor.get[String]("address_reading_city")
        addressReadingLine1       <- hcursor.get[String]("address_reading_line1")
        addressReadingLine2       <- hcursor.get[String]("address_reading_line2")
        bankAccountNumber         <- hcursor.get[String]("bank_account_number")
        bankBranchCode            <- hcursor.get[String]("bank_branch_code")
        bankCode                  <- hcursor.get[String]("bank_code")
        bankPersonName            <- hcursor.get[String]("bank_person_name")
        bankType                  <- hcursor.get[BankAccountType]("bank_type")
        businessCapital           <- hcursor.get[Option[Long]]("business_capital")
        businessDetail            <- hcursor.get[Option[String]]("business_detail")
        businessName              <- hcursor.get[String]("business_name")
        businessReadingName       <- hcursor.get[String]("business_reading_name")
        businessSalesLastYear     <- hcursor.get[Option[Int]]("business_sales_lastyear")
        businessType              <- hcursor.get[BusinessType]("business_type")
        cellPhone                 <- hcursor.get[String]("cell_phone")
        chargeType                <- hcursor.get[Option[ChargeType]]("charge_type")
        contactPerson             <- hcursor.get[Option[String]]("contact_person")
        contactPhone              <- hcursor.get[Option[String]]("contact_phone")
        corporate_number          <- hcursor.get[Option[String]]("corporate_number")
        dateOfEstablishment       <- hcursor.get[String]("date_of_establishment")
        deliveryDetailOpt         <- hcursor.get[Option[String]]("delivery_detail")
        deliveryFeeOpt            <- hcursor.get[Option[Int]]("delivery_fee")
        hasShop                   <- hcursor.get[Option[Boolean]]("has_shop")
        licenseCertFiles          <- hcursor.get[Seq[String]]("license_cert_files")
        openingBusinessCertFiles  <- hcursor.get[Seq[String]]("opening_business_cert_files")
        otherFeeOpt               <- hcursor.get[Option[Int]]("other_fee")
        otherFeeDetailOpt         <- hcursor.get[Option[String]]("other_fee_detail")
        phone                     <- hcursor.get[String]("phone")
        presidentBirthAt          <- hcursor.get[String]("president_birth_at")
        presidentFirstname        <- hcursor.get[String]("president_firstname")
        presidentLastname         <- hcursor.get[String]("president_lastname")
        presidentReadingFirstname <- hcursor.get[String]("president_reading_firstname")
        presidentReadingLastname  <- hcursor.get[String]("president_reading_lastname")
        presidentGender           <- hcursor.get[String]("president_gender")
        privacyPolicyUrl          <- hcursor.get[Option[String]]("privacy_policy_urls")
        productDetail             <- hcursor.get[String]("product_detail")
        productDetailDocument     <- hcursor.get[Seq[String]]("product_detail_document")
        productName               <- hcursor.get[String]("product_name")
        productEnglishName        <- hcursor.get[String]("product_english_name")
        productReadingName        <- hcursor.get[String]("product_reading_name")
        productPriceMin           <- hcursor.get[Int]("product_price_min")
        productPriceMax           <- hcursor.get[Int]("product_price_min")
        productType               <- hcursor.get[Option[String]]("product_type")
        sclAddress                <- hcursor.get[Option[String]]("scl_address")
        sclBusinessName           <- hcursor.get[Option[String]]("scl_business_name")
        sclContactPersonName      <- hcursor.get[Option[String]]("scl_contact_person_name")
        sclEmail                  <- hcursor.get[Option[String]]("scl_email")
        sclOtherFee               <- hcursor.get[Option[Int]]("scl_other_fee")
        sclPaymentMethod          <- hcursor.get[Option[String]]("scl_payment_method")
        sclPhone                  <- hcursor.get[Option[String]]("scl_phone")
        sclReturnAndExchange      <- hcursor.get[Option[String]]("scl_return_and_exchange")
        sclTimeOfDelivery         <- hcursor.get[Option[String]]("scl_time_of_delivery")
        sclTimeOfPayment          <- hcursor.get[Option[String]]("scl_time_of_payment")
        sclUrl                    <- hcursor.get[Option[String]]("scl_url")
        service_start_at          <- hcursor.get[String]("service_start_at")
        shopAddressCity           <- hcursor.get[Option[String]]("shop_address_city")
        shopAddressLine1          <- hcursor.get[Option[String]]("shop_address_line1")
        shopAddressLine2          <- hcursor.get[Option[String]]("shop_address_line2")
        shopAddressReadingCity    <- hcursor.get[Option[String]]("shop_address_reading_city")
        shopAddressReadingLine1   <- hcursor.get[Option[String]]("shop_address_reading_line1")
        shopAddressReadingLine2   <- hcursor.get[Option[String]]("shop_address_reading_line2")
        shopAddressReadingState   <- hcursor.get[Option[String]]("shop_address_reading_state")
        shopAddressState          <- hcursor.get[Option[String]]("shop_address_state")
        shopAddressZip            <- hcursor.get[Option[String]]("shop_address_zip")
        shopPhone                 <- hcursor.get[Option[String]]("shop_phone")
        sitePublished             <- hcursor.get[Option[Boolean]]("site_published")
        sslEnabled                <- hcursor.get[Option[Boolean]]("ssl_enabled")
        termsOfService            <- hcursor.get[Option[String]]("terms_of_service")
        url                       <- hcursor.get[String]("url")
        usingService              <- hcursor.get[Option[String]]("using_service")
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
          businessCapital,
          businessDetail,
          businessName = Name(businessName, businessReadingName),
          businessSalesLastYear,
          businessType,
          cellPhone,
          chargeType,
          contactPerson,
          contactPhone,
          corporate_number,
          dateOfEstablishment,
          hasShop,
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
          presidentFirstName = Name(presidentFirstname, presidentReadingFirstname),
          presidentLastName = Name(presidentLastname, presidentReadingLastname),
          presidentGender,
          privacyPolicyUrl,
          productDetail = ProductDetail(productDetail, productDetailDocument),
          productName = ProductName(productName, productReadingName, productEnglishName),
          productPrice = ProductPrice(productPriceMin, productPriceMax),
          productType,
          sclOpt = {
            (sclAddress,
             sclBusinessName,
             sclContactPersonName,
             sclEmail,
             sclOtherFee,
             sclPaymentMethod,
             sclPhone,
             sclReturnAndExchange,
             sclTimeOfDelivery,
             sclTimeOfPayment,
             sclUrl) match {
              case (Some(address),
                    Some(businessName),
                    Some(contactPersonName),
                    Some(email),
                    Some(otherFee),
                    Some(paymentMethod),
                    Some(phone),
                    Some(returnAndExchange),
                    Some(timeOfDelivery),
                    Some(timeOfPayment),
                    Some(url)) =>
                Some(
                  Scl(businessName,
                      contactPersonName,
                      email,
                      phone,
                      address,
                      otherFee,
                      paymentMethod,
                      timeOfPayment,
                      timeOfDelivery,
                      returnAndExchange,
                      url)
                )
              case _ =>
                None
            }
          },
          service_start_at,
          shopAddressOpt = {
            (shopAddressZip,
             shopAddressState,
             shopAddressCity,
             shopAddressLine1,
             shopAddressLine2,
             shopAddressReadingState,
             shopAddressReadingCity,
             shopAddressReadingLine1,
             shopAddressReadingLine2) match {
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
          shopPhone,
          sitePublished,
          sslEnabled,
          termsOfService,
          url,
          usingService
        )
    }
  }

  case class Merchant(application: Option[Application],
                      bankEnabled: Boolean,
                      brandsAccepted: Seq[String],
                      currenciesSupported: Seq[Currency],
                      defaultCurrency: Currency,
                      country: Option[String],
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
