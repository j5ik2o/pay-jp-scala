package com.github.j5ik2o.payjp.scala

import com.github.j5ik2o.payjp.scala.model.ClientError

case class ClientException(clientErrorResponse: ClientError) extends Exception(s"occurred error: $clientErrorResponse")
