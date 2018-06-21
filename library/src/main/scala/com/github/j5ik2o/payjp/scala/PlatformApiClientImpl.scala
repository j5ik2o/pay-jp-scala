package com.github.j5ik2o.payjp.scala

import java.time.ZonedDateTime

import akka.http.scaladsl.model.{ FormData, HttpMethods, HttpRequest, Uri }
import com.github.j5ik2o.payjp.scala.model._
import monix.eval.Task

class PlatformApiClientImpl(val sender: HttpRequestSender, secretKey: SecretKey)
    extends PlatformApiClient
    with QueryBuildSupport {

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

  override def getPlatformTransfer(platformTransferId: PlatformTransferId): Task[PlatformTransfer] = {
    val method  = HttpMethods.GET
    val uri     = Uri(s"/v1/platform/transfers/${platformTransferId.value}")
    val request = HttpRequest(uri = uri, method = method)
    sender.sendRequest[PlatformTransfer](request, secretKey.value)
  }

  override def getPlatformTransfers(status: Option[TransferStatus],
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
}
