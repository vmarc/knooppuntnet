package kpn.core.db.json

import kpn.shared.Subset
import spray.json.JsString
import spray.json.JsValue
import spray.json.RootJsonFormat
import spray.json.deserializationError

object SubsetFormat extends RootJsonFormat[Subset] {

  def write(subset: Subset): JsValue = JsString(s"${subset.country.domain}:${subset.networkType.name}")

  def read(value: JsValue): Subset = {
    value match {
      case name: JsString =>
        val names = name.value.split(":")
        Subset.of(names.head, names(1)).get
      case _ => deserializationError("Subset expected")
    }
  }
}
