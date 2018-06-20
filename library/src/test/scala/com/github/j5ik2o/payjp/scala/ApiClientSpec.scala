package com.github.j5ik2o.payjp.scala

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.github.j5ik2o.payjp.scala.model.CreateCustomerRequest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.{ BeforeAndAfterAll, FreeSpecLike }
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.duration._

class ApiClientSpec
    extends TestKit(ActorSystem("ApiClientSpec"))
    with FreeSpecLike
    with BeforeAndAfterAll
    with ScalaFutures {
  import system.dispatcher

  override implicit def patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(10, Seconds)), interval = scaled(Span(1, Seconds)))

  override def beforeAll(): Unit = {
    super.beforeAll()

  }

  override def afterAll(): Unit = {
    super.afterAll()
    TestKit.shutdownActorSystem(system)
  }

  val apiClient = new ApiClient(ApiConfig("api.pay.jp", 443, 3 seconds, sys.env("SECRET_KEY")))

  "ApiClient" - {
    "getCustomer" in {
      val result = apiClient
        .createCustomer(
          CreateCustomerRequest(id = None, email = None, description = None, card = None, metaData = Map("a" -> 1))
        )
        .runAsync
        .futureValue
      apiClient.getCustomer(result.id).runAsync.futureValue
    }
  }

}
