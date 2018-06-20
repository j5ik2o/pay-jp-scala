package com.github.j5ik2o.payjp.scala.model

case class CreateCustomerRequest(idOpt: Option[String],
                                 emailOpt: Option[String],
                                 descriptionOpt: Option[String],
                                 cardTokenOpt: Option[CardToken],
                                 metaData: Map[String, Any]) {
  def toMap: Map[String, String] = {
    idOpt.map(v => Map("id"                   -> v)).getOrElse(Map.empty) ++
    emailOpt.map(v => Map("email"             -> v)).getOrElse(Map.empty) ++
    descriptionOpt.map(v => Map("description" -> v)).getOrElse(Map.empty) ++
    cardTokenOpt.map(v => Map("card"          -> v.value)).getOrElse(Map.empty) ++
    metaData
      .map {
        case (key, value) =>
          s"metadata[$key]" -> value.toString
      }
  }
}
