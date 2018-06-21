package com.github.j5ik2o.payjp.scala

import java.time.ZonedDateTime

import com.github.j5ik2o.payjp.scala.model._
import monix.eval.Task

trait PlatformApiClient {
  def createPlatformMerchant(name: String): Task[PlatformMerchant]
  def getPlatformMerchant(merchantId: AccountId): Task[PlatformMerchant]
  def getPlatformMerchants(limit: Option[Int] = None,
                           offset: Option[Int] = None,
                           since: Option[ZonedDateTime] = None,
                           until: Option[ZonedDateTime] = None): Task[Collection[PlatformMerchant]]

  def getPlatformTransfer(platformTransferId: PlatformTransferId): Task[PlatformTransfer]
  def getPlatformTransfers(status: Option[TransferStatus] = None,
                           limit: Option[Int] = None,
                           offset: Option[Int] = None,
                           since: Option[ZonedDateTime] = None,
                           until: Option[ZonedDateTime] = None): Task[Collection[PlatformTransfer]]

  def getMerchantTransfer(platformTransferId: PlatformTransferId, transferId: TransferId): Task[Transfer]
  def getMerchantTransfers(platformTransferId: PlatformTransferId): Task[Collection[Transfer]]
}
