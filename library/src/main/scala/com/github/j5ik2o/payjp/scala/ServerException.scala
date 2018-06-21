package com.github.j5ik2o.payjp.scala

case class ServerException(status: Int, message: String, param: Option[String], code: Option[String])
    extends Exception(s"occurred server error: $status, $message, $param, $code")
