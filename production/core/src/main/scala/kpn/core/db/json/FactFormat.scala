package kpn.core.db.json

import kpn.shared.Fact
import spray.json.JsString
import spray.json.JsValue
import spray.json.RootJsonFormat
import spray.json.deserializationError

object FactFormat extends RootJsonFormat[Fact] {
  def write(fact: Fact): JsValue = JsString(fact.name)

  def read(value: JsValue): Fact = {
    value match {
      case factName: JsString =>
        Fact.withName(factName.value) match {
          case Some(fact) => fact
          case None =>
            deserializationError("Unexpected fact-name " + factName.value)
        }
      case _ => deserializationError("Fact expected")
    }
  }
}
