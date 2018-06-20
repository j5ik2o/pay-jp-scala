package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

case class Transfer(id: String, liveMode: Boolean, created: ZonedDateTime)
