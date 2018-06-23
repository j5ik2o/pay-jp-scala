package com.github.j5ik2o.payjp.scala

import java.time.ZonedDateTime

import akka.http.scaladsl.model.{ FormData, HttpMethods, HttpRequest, Uri }
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

  override def updatePlatformMerchantAdditional(): Task[PlatformMerchant] = ???

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
                                           businessType: String,
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
      "business_type"               -> businessType,
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
      "bank_type"                   -> bank.`type`,
      "bank_account_number"         -> bank.accountNumber,
      "bank_person_name"            -> bank.personName,
    ) ++ usingService.map(v => Map("using_service"                            -> v)).getOrElse(Map.empty) ++
    productDetailDocument.map(v => Map("product_detail_document"              -> v)).getOrElse(Map.empty) ++
    deleteProductDetailDocument.map(v => Map("delete_product_detail_document" -> v.toString)).getOrElse(Map.empty) ++
    scl
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
      .getOrElse(Map.empty) ++
    licenseCert.map(v => Map("license_cert"              -> v)).getOrElse(Map.empty) ++
    deleteLicenseCert.map(v => Map("delete_license_cert" -> v.toString)).getOrElse(Map.empty) ++
    corporateNumber.map(v => Map("corporate_number"      -> v)).getOrElse(Map.empty)

    val request = HttpRequest(uri = path, method = method)
      .withEntity(FormData(params).toEntity)
    sender.sendRequest[PlatformMerchant](request, secretKey.value)
  }
}
