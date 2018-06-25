package com.github.j5ik2o.payjp.scala

import java.time.ZonedDateTime

import akka.http.scaladsl.model._
import com.github.j5ik2o.payjp.scala.model.PlatformMerchant.OtherFee
import com.github.j5ik2o.payjp.scala.model._
import com.github.j5ik2o.payjp.scala.model.merchant._
import monix.eval.Task

class PlatformApiClientImpl(val sender: HttpRequestSender, secretKey: SecretKey)
    extends PlatformApiClient
    with QueryBuildSupport {
  override def getAccount(): Task[Account] = {
    val method  = HttpMethods.GET
    val uri     = Uri(s"/v1/accounts")
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Account](request, secretKey.value)
  }
  override def createPlatformMerchant(name: String): Task[PlatformMerchant] = {
    val method = HttpMethods.POST
    val path   = s"/v1/platform/merchants"
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(Map("display_name" -> name)).toEntity)
    sender.sendRequest[PlatformMerchant](request, secretKey.value)
  }

  override def getPlatformMerchant(merchantId: AccountId): Task[PlatformMerchant] = {
    val method  = HttpMethods.GET
    val path    = s"/v1/platform/merchants/${merchantId.value}"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[PlatformMerchant](request, secretKey.value)
  }

  override def getPlatformMerchants(limit: Option[Int],
                                    offset: Option[Int],
                                    since: Option[ZonedDateTime],
                                    until: Option[ZonedDateTime]): Task[Collection[PlatformMerchant]] = {
    val method  = HttpMethods.GET
    val params  = getParamMap(limit, offset, since, until)
    val uri     = Uri("/v1/platform/merchants").withQuery(Uri.Query(params))
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Collection[PlatformMerchant]](request, secretKey.value)
  }

  override def updatePlatformMerchantKeys(accountId: AccountId,
                                          keytype: ApiKeyType,
                                          accessmode: String,
                                          timing: String): Task[PlatformMerchant] = {
    val method = HttpMethods.POST
    val path   = s"/v1/platform/merchants/${accountId.value}/keys"
    val params = Map("keytype" -> keytype.entryName, "accessmode" -> accessmode, "timing" -> timing)
    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[PlatformMerchant](request, secretKey.value)
  }

  override def deletePlatformMerchant(accountId: AccountId): Task[Deleted[AccountId]] = {
    val method  = HttpMethods.DELETE
    val path    = s"/v1/platform/merchants/${accountId.value}"
    val request = HttpRequest(uri = path, method = method)
    sender.sendRequest[Deleted[AccountId]](request, secretKey.value)
  }

  // ---

  override def getPlatformTransfer(platformTransferId: PlatformTransferId): Task[PlatformTransfer] = {
    val method  = HttpMethods.GET
    val uri     = Uri(s"/v1/platform/transfers/${platformTransferId.value}")
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[PlatformTransfer](request, secretKey.value)
  }

  override def getPlatformTransfers(status: Option[TransferStatusType],
                                    limit: Option[Int],
                                    offset: Option[Int],
                                    since: Option[ZonedDateTime],
                                    until: Option[ZonedDateTime]): Task[Collection[PlatformTransfer]] = {
    val method = HttpMethods.GET
    val params = getParamMap(limit, offset, since, until) ++ status
      .map(v => Map("status" -> v.entryName))
      .getOrElse(Map.empty)
    val uri     = Uri("/v1/platform/transfers").withQuery(Uri.Query(params))
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Collection[PlatformTransfer]](request, secretKey.value)

  }

  override def getMerchantTransfer(platformTransferId: PlatformTransferId, transferId: TransferId): Task[Transfer] = {
    val method  = HttpMethods.GET
    val uri     = Uri(s"v1/platform/transfers/${platformTransferId.value}/transfers/${transferId.value}")
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Transfer](request, secretKey.value)
  }

  override def getMerchantTransfers(platformTransferId: PlatformTransferId): Task[Collection[Transfer]] = {
    val method  = HttpMethods.GET
    val uri     = Uri(s"v1/platform/transfers/${platformTransferId.value}/transfers")
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[Collection[Transfer]](request, secretKey.value)
  }

  override def updatePlatformMerchantBasic(accountId: AccountId,
                                           dryRun: Boolean,
                                           productName: ProductName,
                                           url: String,
                                           serviceStartAt: String,
                                           usingService: Option[String],
                                           productDetail: String,
                                           productDetailDocument: Option[String],
                                           deleteProductDetailDocument: Option[Int],
                                           productPrice: ProductPrice,
                                           businessType: BusinessType,
                                           businessName: Name,
                                           dateOfEstablishment: String,
                                           businessCapital: Int,
                                           presidentLastName: Name,
                                           presidentFirstName: Name,
                                           presidentBirthAt: String,
                                           presidentGender: GenderType,
                                           address: Address,
                                           contact: Contact,
                                           scl: Option[Scl],
                                           bank: Bank,
                                           corporateNumber: Option[String],
                                           licenseCert: Option[String],
                                           deleteLicenseCert: Option[Int]): Task[PlatformMerchant] = {
    val method = HttpMethods.POST
    val path   = s"/v1/platform/merchants/${accountId.value}/applications/basic"
    val params = Map(
      "dryrun"                      -> dryRun.toString,
      "product_name"                -> productName.value,
      "product_reading_name"        -> productName.readingName,
      "product_english_name"        -> productName.englishName,
      "url"                         -> url,
      "service_start_at"            -> serviceStartAt,
      "product_detail"              -> productDetail,
      "product_price_min"           -> productPrice.min.toString,
      "product_price_max"           -> productPrice.max.toString,
      "business_type"               -> businessType.entryName,
      "business_name"               -> businessName.value,
      "business_reading_name"       -> businessName.readingName,
      "date_of_establishment"       -> dateOfEstablishment,
      "business_capital"            -> businessCapital.toString,
      "president_lastname"          -> presidentLastName.value,
      "president_firstname"         -> presidentFirstName.value,
      "president_reading_lastname"  -> presidentLastName.readingName,
      "president_reading_firstname" -> presidentFirstName.readingName,
      "president_birth_at"          -> presidentBirthAt,
      "president_gender"            -> presidentGender.entryName,
      "address_zip"                 -> address.zip,
      "address_state"               -> address.state.value,
      "address_city"                -> address.city.value,
      "address_line1"               -> address.line1.value,
      "address_line2"               -> address.line2.value,
      "address_reading_state"       -> address.state.readingName,
      "address_reading_city"        -> address.city.readingName,
      "address_reading_line1"       -> address.line1.readingName,
      "address_reading_line2"       -> address.line2.readingName,
      "phone"                       -> contact.phone,
      "cell_phone"                  -> contact.cellPhone,
      "has_scl_url"                 -> scl.isEmpty.toString,
      "bank_code"                   -> bank.code,
      "bank_branch_code"            -> bank.branchCode,
      "bank_type"                   -> bank.`type`.entryName,
      "bank_account_number"         -> bank.accountNumber,
      "bank_person_name"            -> bank.personName
    ) ++ usingService
      .map(v => Map("using_service" -> v))
      .getOrElse(Map.empty) ++ productDetailDocument
      .map(v => Map("product_detail_document" -> v))
      .getOrElse(Map.empty) ++ deleteProductDetailDocument
      .map(v => Map("delete_product_detail_document" -> v.toString))
      .getOrElse(Map.empty) ++ scl
      .map { v =>
        Map(
          "scl_business_name"       -> v.businessName,
          "scl_contact_person_name" -> v.contactPersonName,
          "scl_email"               -> v.email,
          "scl_phone"               -> v.phone,
          "scl_address"             -> v.address,
          "scl_other_fee"           -> v.otherFee.toString,
          "scl_payment_method"      -> v.paymentMethod,
          "scl_time_of_payment"     -> v.timeOfPayment,
          "scl_time_of_delivery"    -> v.timeOfDelivery,
          "scl_return_and_exchange" -> v.returnAndExchange,
          "scl_url"                 -> v.url
        )
      }
      .getOrElse(Map.empty) ++ licenseCert
      .map(v => Map("license_cert" -> v))
      .getOrElse(Map.empty) ++ deleteLicenseCert
      .map(v => Map("delete_license_cert" -> v.toString))
      .getOrElse(Map.empty) ++ corporateNumber
      .map(v => Map("corporate_number" -> v))
      .getOrElse(Map.empty)

    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[PlatformMerchant](request, secretKey.value)
  }

  override def updatePlatformMerchantAdditional(accountId: AccountId,
                                                dryRun: Boolean,
                                                productType: ProductType,
                                                chargeType: ChargeType,
                                                soleProp: Boolean,
                                                dateOfEstablishment: Option[String],
                                                openingBusinessCert: Option[String],
                                                sitePublished: Boolean,
                                                businessSalesLastYear: Int,
                                                businessDetail: String,
                                                shopAddress: Option[Address],
                                                shopPhone: Option[String],
                                                privacyPolicyUrl: String,
                                                sslEnabled: Boolean,
                                                otherFee: Option[OtherFee],
                                                contactPersonLastName: String,
                                                contactPersonFirstName: String,
                                                contactPhone: String): Task[PlatformMerchant] = {
    val method = HttpMethods.POST
    val path   = s"/v1/platform/merchants/${accountId.value}/applications/additional"
    val params =
    Map(
      "dryrun"                   -> dryRun.toString,
      "product_type"             -> productType.entryName,
      "charge_type"              -> chargeType.entryName,
      "sole_prop"                -> soleProp.toString,
      "site_published"           -> sitePublished.toString,
      "business_sales_lastyear"  -> businessSalesLastYear.toString,
      "business_detail"          -> businessDetail,
      "has_shop"                 -> shopAddress.isDefined.toString,
      "privacy_policy_url"       -> privacyPolicyUrl,
      "ssl_enabled"              -> sslEnabled.toString,
      "has_other_fee"            -> otherFee.isDefined.toString,
      "contact_person_lastname"  -> contactPersonLastName,
      "contact_person_firstname" -> contactPersonFirstName,
      "contact_phone"            -> contactPhone
    ) ++ dateOfEstablishment.map(v => Map("date_of_establishment" -> v)).getOrElse(Map.empty) ++
    openingBusinessCert.map(v => Map("opening_business_cert"      -> v)).getOrElse(Map.empty) ++
    shopAddress
      .map(
        v =>
          Map(
            "shop_address_zip"           -> v.zip,
            "shop_address_state"         -> v.state.value,
            "shop_address_city"          -> v.city.value,
            "shop_address_line1"         -> v.line1.value,
            "shop_address_line2"         -> v.line2.value,
            "shop_address_reading_state" -> v.state.readingName,
            "shop_address_reading_city"  -> v.city.readingName,
            "shop_address_reading_line1" -> v.line1.readingName,
            "shop_address_reading_line2" -> v.line2.readingName
        )
      )
      .getOrElse(Map.empty) ++
    shopPhone.map(v => Map("shop_phone" -> v)).getOrElse(Map.empty) ++
    otherFee
      .map { v =>
        Map(
          "delivery_fee"     -> v.deliveryFee.toString,
          "delivery_detail"  -> v.deliveryDetail,
          "other_fee"        -> v.otherFee.toString,
          "other_fee_detail" -> v.otherFeeDetail.toString
        )
      }
      .getOrElse(Map.empty)

    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[PlatformMerchant](request, secretKey.value)
  }

}
