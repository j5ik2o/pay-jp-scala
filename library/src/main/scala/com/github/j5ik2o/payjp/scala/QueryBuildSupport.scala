package com.github.j5ik2o.payjp.scala

import java.time.ZonedDateTime

trait QueryBuildSupport {
  def getParamMap(limit: Option[Int],
                  offset: Option[Int],
                  since: Option[ZonedDateTime],
                  until: Option[ZonedDateTime]): Map[String, String] = {
    val params = limit.map(v => Map("limit" -> v.toString)).getOrElse(Map.empty) ++
    offset.map(v => Map("offset" -> v.toString)).getOrElse(Map.empty) ++
    since.map(v => Map("since"   -> v.toEpochSecond.toString)).getOrElse(Map.empty) ++
    until.map(v => Map("until"   -> v.toEpochSecond.toString)).getOrElse(Map.empty)
    params
  }
}
