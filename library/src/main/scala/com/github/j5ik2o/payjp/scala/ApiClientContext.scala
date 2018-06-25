package com.github.j5ik2o.payjp.scala

import akka.actor.ActorSystem
import com.github.j5ik2o.payjp.scala.model.SecretKey

import scala.concurrent.duration.FiniteDuration

class ApiClientContext(val host: String,
                       val port: Int = 443,
                       val timeoutForToStrict: FiniteDuration,
                       val requestBufferSize: Int = 20)(
    implicit system: ActorSystem
) {

  val sender: HttpRequestSender = new HttpRequestSenderImpl(this)

  def createMerchantApiClient(merchantSecretKey: SecretKey): MerchantApiClient = {
    new MerchantApiClientImpl(sender, merchantSecretKey)
  }

  def createPlatformApiClient(platformSecretKey: SecretKey): PlatformApiClient = {
    new PlatformApiClientImpl(sender, platformSecretKey)
  }

}
