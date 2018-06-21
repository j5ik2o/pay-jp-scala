package com.github.j5ik2o.payjp.scala

import akka.actor.ActorSystem
import com.github.j5ik2o.payjp.scala.model.SecretKey

class ApiClientBuilder(config: ApiConfig)(implicit system: ActorSystem) {
  private val sender = new HttpRequestSender(config)

  def createMerchantApiClient(merchantSecretKey: SecretKey): MerchantApiClient = {
    new MerchantApiClientImpl(sender, merchantSecretKey)
  }

  def createPlatformApiClient(platformSecretKey: SecretKey): PlatformApiClient = {
    new PlatformApiClientImpl(sender, platformSecretKey)
  }

}
