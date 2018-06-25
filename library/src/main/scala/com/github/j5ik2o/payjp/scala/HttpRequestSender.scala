package com.github.j5ik2o.payjp.scala

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.headers.RawHeader
import io.circe.Decoder
import monix.eval.Task

trait HttpRequestSender {
  def shutdown(): Unit
  def sendRequest[A: Decoder](request: HttpRequest, secretKey: String, headers: List[RawHeader] = List.empty): Task[A]
}
