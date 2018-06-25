package com.github.j5ik2o.payjp.scala

case class ServerException(status: Int, message: String, paramOpt: Option[String], codeOpt: Option[String])
    extends Exception(s"occurred server error: $status, $message, $paramOpt, $codeOpt")
