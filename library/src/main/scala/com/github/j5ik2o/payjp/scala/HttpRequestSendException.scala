package com.github.j5ik2o.payjp.scala

case class HttpRequestSendException(message: String, cause: Option[Throwable] = None) extends Exception(message)
