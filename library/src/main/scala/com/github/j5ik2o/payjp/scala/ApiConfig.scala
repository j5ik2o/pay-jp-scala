package com.github.j5ik2o.payjp.scala

import scala.concurrent.duration.FiniteDuration

case class ApiConfig(host: String, port: Int = 443, timeoutForToStrict: FiniteDuration, requestBufferSize: Int = 20)
