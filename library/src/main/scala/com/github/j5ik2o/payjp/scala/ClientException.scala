package com.github.j5ik2o.payjp.scala

import com.github.j5ik2o.payjp.scala.model.ClientErrorResponse

case class ClientException(clientErrorResponse: ClientErrorResponse)
    extends Exception(s"occurred error: $clientErrorResponse")
