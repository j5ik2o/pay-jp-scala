package com.github.j5ik2o.payjp.scala

import akka.actor.ActorSystem
import com.github.j5ik2o.payjp.scala.model.SecretKey
import scala.concurrent.duration._

class ApiClientContext(val host: String,
                       val port: Int = 443,
                       val timeoutForToStrict: FiniteDuration = 3 seconds,
                       val requestBufferSize: Int = 64)(
    implicit system: ActorSystem
) {

  val sender: HttpRequestSender = new HttpRequestSenderImpl(this)

  def shutdownSender(): Unit = sender.shutdown()

  def createMerchantApiClient(merchantSecretKey: SecretKey): MerchantApiClient = {
    new MerchantApiClientImpl(sender, merchantSecretKey)
  }

  def createPlatformApiClient(platformSecretKey: SecretKey): PlatformApiClient = {
    new PlatformApiClientImpl(sender, platformSecretKey)
  }

}
