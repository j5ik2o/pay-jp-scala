package com.github.j5ik2o.payjp.scala

import akka.http.scaladsl.model.headers.RawHeader

trait ApiClientSupport {

  val testHeaders = List(RawHeader("X-Payjp-Direct-Token-Generate", "true"))

}
