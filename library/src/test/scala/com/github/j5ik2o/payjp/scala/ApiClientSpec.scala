package com.github.j5ik2o.payjp.scala

import java.util.UUID

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.github.j5ik2o.payjp.scala.model._
import monix.execution.Scheduler.Implicits.global
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.{ BeforeAndAfterAll, FreeSpecLike, Matchers }

import scala.concurrent.duration._
import cats.implicits._

class ApiClientSpec
    extends TestKit(ActorSystem("ApiClientSpec"))
    with FreeSpecLike
    with BeforeAndAfterAll
    with ScalaFutures
    with Matchers {

  override implicit def patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(10, Seconds)), interval = scaled(Span(1, Seconds)))

  override def beforeAll(): Unit = {
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    super.afterAll()
    TestKit.shutdownActorSystem(system)
  }

  val apiClientBuilder: ApiClientBuilder = new ApiClientBuilder(ApiConfig("api.pay.jp", 443, 3 seconds))
  val merchantApiClient: MerchantApiClient =
    apiClientBuilder.createMerchantApiClient(SecretKey(sys.env("MERCHANT_SECRET_KEY")))
  val platformApiClient: PlatformApiClient =
    apiClientBuilder.createPlatformApiClient(SecretKey(sys.env("PLATFORM_SECRET_KEY")))

  "ApiClient" - {
    "Account" in {
      val account = merchantApiClient.getAccount().runAsync.futureValue
      println(account)
    }
    "Plan" in {
      val plan1 =
        merchantApiClient
          .createPlan(Amount(BigDecimal(1000L)), Currency("jpy"), PlanInterval.Month)
          .runAsync
          .futureValue
      val plan2: Plan = merchantApiClient.getPlan(plan1.id).runAsync.futureValue
      plan2 shouldBe plan1
    }
    "Product" in {
      val product1 = merchantApiClient.createProduct(Amount(100L), Currency("jpy")).runAsync.futureValue
      val product2 = merchantApiClient.getProduct(product1.id).runAsync.futureValue
      product2 shouldBe product1
      val products = merchantApiClient.getProducts().runAsync.futureValue
      products.data.exists(_.id == product1.id) shouldBe true
      val product3 = merchantApiClient.updateProduct(product1.id, None, Map("a" -> "1")).runAsync.futureValue
      product3.metadata should not be product1.metadata
      val deleted = merchantApiClient.deleteProduct(product1.id).runAsync.futureValue
      deleted.deleted shouldBe true
      deleted.id shouldBe product1.id
    }
    "Customer" in {
      val customer1: Customer = merchantApiClient
        .createCustomer(customerIdOpt = None,
                        emailOpt = None,
                        descriptionOpt = None,
                        cardTokenOpt = None,
                        metadata = Map("a" -> 1))
        .runAsync
        .futureValue
      val customer2 = merchantApiClient.getCustomer(customer1.id).runAsync.futureValue
      customer2 shouldBe customer1
      val customers = merchantApiClient.getCustomers().runAsync.futureValue
      customers.data.exists(_.id == customer1.id) shouldBe true
      val customer3 =
        merchantApiClient.updateCustomer(customer1.id, emailOpt = Some("test@test.com")).runAsync.futureValue
      customer3.email should not be customer1.email
      val deleted = merchantApiClient.deleteCustomer(customer1.id).runAsync.futureValue
      deleted.deleted shouldBe true
      deleted.id shouldBe customer1.id
    }
//    "Subscription" in {
//      val plan1 =
//        merchantApiClient
//          .createPlan(Amount(BigDecimal(1000L)), Currency("jpy"), PlanInterval.Month)
//          .runAsync
//          .futureValue
//      val customer1: Customer = merchantApiClient
//        .createCustomer(customerIdOpt = None,
//                        emailOpt = None,
//                        descriptionOpt = None,
//                        cardTokenOpt = None,
//                        metadata = Map("a" -> 1))
//        .runAsync
//        .futureValue
//      val subscription1 = merchantApiClient.createSubscription(customer1.id, plan1.id).runAsync.futureValue
//      val subscription2 = merchantApiClient.getSubscriptionByCustomerId(customer1.id, subscription1.id)
//      subscription2 shouldBe subscription1
//    }
    "Transfer" in {
      val merchantApiClient: MerchantApiClient =
        apiClientBuilder.createMerchantApiClient(SecretKey("sk_test_c62fade9d045b54cd76d7036"))
      val transferId = TransferId("tr_8f0c0fe2c9f8a47f9d18f03959ba1")
      val transfer1  = merchantApiClient.getTransfer(transferId).runAsync.futureValue
      val transfers  = merchantApiClient.getTransfers().runAsync.futureValue
      transfers.data.toList should contain(transfer1)
      val charges = merchantApiClient.getChargesByTransferId(transferId).runAsync.futureValue
      charges.isEmpty shouldBe true
    }
    "getCharge" in {}
    "Platform" - {
      "PlatformMerchant" in {
        val platformMerchant1 =
          platformApiClient.createPlatformMerchant(UUID.randomUUID().toString).runAsync.futureValue
        val platformMerchant2 = platformApiClient.getPlatformMerchant(platformMerchant1.id).runAsync.futureValue
        platformMerchant2 shouldBe platformMerchant1
        val platformMerchants = platformApiClient.getPlatformMerchants().runAsync.futureValue
        platformMerchants.count should be > 0
        platformMerchants.data.size shouldBe platformMerchants.count
        platformMerchants.data should contain(platformMerchant1)
      }
      "" in {
        val platformMerchant1: PlatformMerchant =
          platformApiClient.createPlatformMerchant(UUID.randomUUID().toString).runAsync.futureValue
        val merchantApiClient: MerchantApiClient =
          apiClientBuilder.createMerchantApiClient(platformMerchant1.keys.testSecretKey)
        merchantApiClient.getAccount().runAsync.futureValue
      }
    }
  }

}
