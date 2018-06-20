package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

case class Subscription(id: String,
                        status: String,
                        livemode: Boolean,
                        created: ZonedDateTime,
                        start: ZonedDateTime,
                        customerId: String,
                        plan: Plan,
                        prorate: Boolean,
                        currentPeriodStart: ZonedDateTime,
                        currentPeriodEnd: ZonedDateTime,
                        trialStart: ZonedDateTime,
                        trialEnd: ZonedDateTime,
                        pausedAt: ZonedDateTime,
                        canceledAt: ZonedDateTime,
                        resumedAt: ZonedDateTime,
                        metadata: Map[String, Any]) {
  val `object` = "subscription"
}
