package com.github.j5ik2o.payjp.scala

case class CardException(status: Int, message: String, code: String)
    extends Exception(s"occurred card error: $status, $message, $code")
