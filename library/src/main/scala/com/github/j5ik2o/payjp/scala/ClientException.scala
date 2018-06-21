package com.github.j5ik2o.payjp.scala

case class ClientException(status: Int, message: String, param: Option[String], code: Option[String])
    extends Exception(s"occurred client error: $status, $message, $param, $code")
