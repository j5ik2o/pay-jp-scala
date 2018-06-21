package com.github.j5ik2o.payjp.scala

case class AuthException(status: Int, message: String) extends Exception(s"occurred auth error: $status, $message")
