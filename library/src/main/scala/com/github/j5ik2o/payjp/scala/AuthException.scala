package com.github.j5ik2o.payjp.scala

import com.github.j5ik2o.payjp.scala.model.AuthErrorResponse

case class AuthException(errorResponse: AuthErrorResponse) extends Exception(s"occurred error: $errorResponse")
