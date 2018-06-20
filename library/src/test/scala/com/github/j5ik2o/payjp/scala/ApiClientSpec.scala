package com.github.j5ik2o.payjp.scala

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.github.j5ik2o.payjp.scala.model._
import jp.pay.{ Payjp, SetApiKey }
import jp.pay.model.Token
import monix.execution.Scheduler.Implicits.global
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.{ BeforeAndAfterAll, FreeSpecLike, Matchers }

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

  val apiClient = ApiClient(ApiConfig("api.pay.jp", 443, 3 seconds, sys.env("SECRET_KEY")))

  "ApiClient" - {
    "createPlan && get Plan" in {
      val plan1 =
        apiClient.createPlan(Amount(BigDecimal(1000L)), Currency("jpy"), PlanInterval.Month).runAsync.futureValue
      val plan2: Plan = apiClient.getPlan(plan1.id).runAsync.futureValue
      plan2.id shouldBe plan1.id
      plan2.amount shouldBe plan1.amount
      plan2.currency shouldBe plan1.currency
      plan2.interval shouldBe plan1.interval
      plan2.nameOpt shouldBe plan1.nameOpt
      plan2.trialDaysOpt shouldBe plan1.trialDaysOpt
      plan2.billingDayOpt shouldBe plan1.billingDayOpt
      plan2.metaData shouldBe plan1.metaData
    }
    "getCustomer" in {
      val customer1: Customer = apiClient
        .createCustomer(customerIdOpt = None,
                        emailOpt = None,
                        descriptionOpt = None,
                        cardTokenOpt = None,
                        metaData = Map("a" -> 1))
        .runAsync
        .futureValue
      val customer2 = apiClient.getCustomer(customer1.id).runAsync.futureValue
      customer2.id shouldBe customer1.id
      customer2.liveMode shouldBe customer1.liveMode
      customer2.email shouldBe customer1.email
      customer2.description shouldBe customer1.description
      customer2.defaultCardId shouldBe customer1.defaultCardId
      customer2.metaData shouldBe customer1.metaData
      customer2.cards shouldBe customer1.cards
      customer2.created shouldBe customer1.created
      val customers = apiClient.getCustomers().runAsync.futureValue
      customers.data.exists(_.id == customer1.id) shouldBe true
      val customer3 = apiClient.updateCustomer(customer1.id, emailOpt = Some("test@test.com")).runAsync.futureValue
      customer3.email should not be customer1.email
      val deleted = apiClient.deleteCustomer(customer1.id).runAsync.futureValue
      deleted.deleted shouldBe true
      deleted.id shouldBe customer1.id
    }
    "getCharge" in {}
  }

}
