package com.github.j5ik2o.payjp.scala.model

case class CreatePlanRequest(amount: PlanAmount,
                             currency: Currency,
                             interval: PlanInterval,
                             idOpt: Option[String],
                             nameOpt: Option[String],
                             trialDaysOpt: Option[Int],
                             billingDayOpt: Option[Int],
                             metadata: Map[String, String])
