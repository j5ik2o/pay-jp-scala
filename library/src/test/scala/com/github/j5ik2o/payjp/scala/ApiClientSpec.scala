package com.github.j5ik2o.payjp.scala

import java.util.UUID

import akka.actor.ActorSystem
import akka.testkit.TestKit
import cats.implicits._
import com.github.j5ik2o.payjp.scala.model._
import com.github.j5ik2o.payjp.scala.model.merchant._
import monix.execution.Scheduler.Implicits.global
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.{ BeforeAndAfterAll, FreeSpecLike, Matchers }

import scala.concurrent.Await
import scala.concurrent.duration._

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
      account.id.value should not be empty
    }
    "Plan" in {
      val plan1 =
        merchantApiClient
          .createPlan(Amount(BigDecimal(1000L)), Currency("jpy"), PlanIntervalType.Month)
          .runAsync
          .futureValue
      val plan2: Plan = merchantApiClient.getPlan(plan1.id).runAsync.futureValue
      plan2 shouldBe plan1
      val plans = merchantApiClient.getPlans().runAsync.futureValue
      plans.data should contain(plan1)
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
                        tokenIdOpt = None,
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
    "Subscription" in {
      val plan1 =
        merchantApiClient
          .createPlan(Amount(BigDecimal(1000L)), Currency("jpy"), PlanIntervalType.Month)
          .runAsync
          .futureValue
      val token1: Token = merchantApiClient
        .createTestToken(number = "4242424242424242", expMonth = 12, expYear = 2020, cvcOpt = Some("123"))
        .runAsync
        .futureValue
      val customer1: Customer = merchantApiClient
        .createCustomer(customerIdOpt = None,
                        emailOpt = None,
                        descriptionOpt = None,
                        tokenIdOpt = Some(token1.id),
                        metadata = Map("a" -> 1))
        .runAsync
        .futureValue

      val subscription1 = merchantApiClient.createSubscription(customer1.id, plan1.id).runAsync.futureValue
      val subscription2 =
        merchantApiClient.getSubscriptionByCustomerId(customer1.id, subscription1.id).runAsync.futureValue
      subscription2 shouldBe subscription1
    }
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
    "Event" in {
      val events = merchantApiClient.getEvents().runAsync.futureValue
      val event1 = merchantApiClient.getEvent(events.data.head.id)
      // events.data should contain(event1)
    }
    def testToken(cardNumber: String, expMonth: Int, expYear: Int, cvc: Some[String]) = {
      val token1: Token =
        merchantApiClient
          .createTestToken(cardNumber, expMonth, expYear, cvc)
          .runAsync
          .futureValue
      val token2 = merchantApiClient.getTestToken(token1.id).runAsync.futureValue
      token2 shouldBe token1
    }
    "Token" - {
      "Visa - 1" in {
        val cardNumber = "4242424242424242"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        testToken(cardNumber, expMonth, expYear, cvc)
      }
      "Visa - 2" in {
        val cardNumber = "4012888888881881"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        testToken(cardNumber, expMonth, expYear, cvc)
      }
      "MasterCard - 1" in {
        val cardNumber = "5555555555554444"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        testToken(cardNumber, expMonth, expYear, cvc)
      }
      "MasterCard - 2" in {
        val cardNumber = "5105105105105100"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        testToken(cardNumber, expMonth, expYear, cvc)
      }
      "JCB - 1" in {
        val cardNumber = "3530111333300000"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        testToken(cardNumber, expMonth, expYear, cvc)
      }
      "JCB - 2" in {
        val cardNumber = "3566002020360505"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        testToken(cardNumber, expMonth, expYear, cvc)
      }
      "American Express - 1" in {
        val cardNumber = "378282246310005"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        testToken(cardNumber, expMonth, expYear, cvc)
      }
      "American Express - 2" in {
        val cardNumber = "371449635398431"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        testToken(cardNumber, expMonth, expYear, cvc)
      }
      "Diners Club - 1" in {
        val cardNumber = "38520000023237"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        testToken(cardNumber, expMonth, expYear, cvc)
      }
      "Diners Club - 2" in {
        val cardNumber = "30569309025904"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        testToken(cardNumber, expMonth, expYear, cvc)
      }
      "Discover - 1" in {
        val cardNumber = "6011111111111117"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        testToken(cardNumber, expMonth, expYear, cvc)
      }
      "Discover - 2" in {
        val cardNumber = "6011000990139424"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        testToken(cardNumber, expMonth, expYear, cvc)
      }
      "card_declined" in {
        val cardNumber = "4000000000000002"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        val thrown = the[CardException] thrownBy {
          Await.result(merchantApiClient
                         .createTestToken(cardNumber, expMonth, expYear, cvc)
                         .runAsync,
                       Duration.Inf)
        }
        thrown.code shouldBe "card_declined"
      }
      "expired_card" in {
        val cardNumber = "4000000000000069"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        val thrown = the[CardException] thrownBy {
          Await.result(merchantApiClient
                         .createTestToken(cardNumber, expMonth, expYear, cvc)
                         .runAsync,
                       Duration.Inf)
        }
        thrown.code shouldBe "expired_card"
      }
      "invalid_cvc" in {
        val cardNumber = "4000000000000127"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        val thrown = the[CardException] thrownBy {
          Await.result(merchantApiClient
                         .createTestToken(cardNumber, expMonth, expYear, cvc)
                         .runAsync,
                       Duration.Inf)
        }
        thrown.code shouldBe "invalid_cvc"
      }
      "processing_error" in {
        val cardNumber = "4000000000000119"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        val thrown = the[CardException] thrownBy {
          Await.result(merchantApiClient
                         .createTestToken(cardNumber, expMonth, expYear, cvc)
                         .runAsync,
                       Duration.Inf)
        }
        thrown.code shouldBe "processing_error"
      }
      "card_declined in charge - 1" in {
        val cardNumber = "4000000000080319"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        testToken(cardNumber, expMonth, expYear, cvc)
      }
      "card_declined in charge - 2" in {
        val cardNumber = "4000000000080202"
        val expMonth   = 12
        val expYear    = 2020
        val cvc        = Some("123")
        testToken(cardNumber, expMonth, expYear, cvc)
      }
    }
    "Charge" - {
      "create Charge" in {
        val token1: Token =
          merchantApiClient
            .createTestToken(number = "4242424242424242", expMonth = 12, expYear = 2020, cvcOpt = Some("123"))
            .runAsync
            .futureValue
        val charge1 = merchantApiClient
          .createCharge(Some((Amount(10000L), Currency("jpy"))), None, None, tokenIdOpt = Some(token1.id))
          .runAsync
          .futureValue
        val charge2 = merchantApiClient.getCharge(charge1.id).runAsync.futureValue
        charge2 shouldBe charge1
        val charges = merchantApiClient.getCharges().runAsync.futureValue
        charges.data should contain(charge1)
      }
      "capture Charge" in {
        val token1: Token =
          merchantApiClient
            .createTestToken(number = "4242424242424242", expMonth = 12, expYear = 2020, cvcOpt = Some("123"))
            .runAsync
            .futureValue
        val charge1 = merchantApiClient
          .createCharge(Some((Amount(10000L), Currency("jpy"))),
                        None,
                        None,
                        tokenIdOpt = Some(token1.id),
                        captureOpt = Some(false))
          .runAsync
          .futureValue
        val charge2 = merchantApiClient.getCharge(charge1.id).runAsync.futureValue
        charge2 shouldBe charge1
        val charges = merchantApiClient.getCharges().runAsync.futureValue
        charges.data should contain(charge1)
        val charge3 = merchantApiClient.captureCharge(charge1.id).runAsync.futureValue
        charge3.captured shouldBe true
      }
      "refund Charge" in {
        val token1: Token =
          merchantApiClient
            .createTestToken(number = "4242424242424242", expMonth = 12, expYear = 2020, cvcOpt = Some("123"))
            .runAsync
            .futureValue
        val charge1 = merchantApiClient
          .createCharge(Some((Amount(10000L), Currency("jpy"))), None, None, tokenIdOpt = Some(token1.id))
          .runAsync
          .futureValue
        val charge2 = merchantApiClient.getCharge(charge1.id).runAsync.futureValue
        charge2 shouldBe charge1
        val charges = merchantApiClient.getCharges().runAsync.futureValue
        charges.data should contain(charge1)
        val charge3 = merchantApiClient.refundCharge(charge1.id).runAsync.futureValue
        charge3.refunded shouldBe true
      }
    }
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
      "account" in {
        val account = merchantApiClient.getAccount().runAsync.futureValue
        platformApiClient
          .updatePlatformMerchantBasic(
            account.id,
            dryRun = true,
            productName = ProductName("test", "テスト", "test"),
            url = "http://test.com",
            serviceStartAt = "2018-01",
            usingService = None,
            productDetail = "テストサービス",
            productDetailDocument = None,
            deleteProductDetailDocument = None,
            productPrice = ProductPrice(1000, 5000),
            businessType = "company",
            businessName = Name("test", "test"),
            dateOfEstablishment = "2009-12-11",
            businessCapital = 100,
            presidentLastName = Name("やまだ", "Yamada"),
            presidentFirstName = Name("たろう", "Taro"),
            presidentBirthAt = "1972-02-03",
            presidentGender = GenderType.Men,
            address = Address(
              zip = "000-0000",
              state = Name("東京都", "tokyo-to"),
              city = Name("ほげほげ", "hogehoge"),
              line1 = Name("ほげほげ", "hogehoge"),
              line2 = Name("1-1-1", "1-1-1")
            ), // Address,
            contact = Contact("03-0000-0000", "090-0000-0000"),
            scl = None,
            bank = Bank(
              code = "0036",
              branchCode = "210",
              `type` = "普通",
              accountNumber = "1234567",
              personName = "Yamada Taro"
            ), // Bank,
            corporateNumber = Some("1234567890123"),
            licenseCert = None,
            deleteLicenseCert = None
          )
          .runAsync
          .futureValue

      }
      "testSecretKey" in {
        val platformMerchant1: PlatformMerchant =
          platformApiClient.createPlatformMerchant(UUID.randomUUID().toString).runAsync.futureValue
        val merchantApiClient: MerchantApiClient =
          apiClientBuilder.createMerchantApiClient(platformMerchant1.keys.get.testSecretKey)
        merchantApiClient.getAccount().runAsync.futureValue
      }
    }
  }

}
