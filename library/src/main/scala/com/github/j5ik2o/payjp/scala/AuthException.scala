package com.github.j5ik2o.payjp.scala

import com.github.j5ik2o.payjp.scala.model.AuthError

case class AuthException(errorResponse: AuthError) extends Exception(s"occurred error: $errorResponse")
