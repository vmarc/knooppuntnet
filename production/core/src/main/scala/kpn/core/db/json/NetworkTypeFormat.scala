package kpn.core.db.json

import kpn.shared.NetworkType
import spray.json.DeserializationException
import spray.json.JsString
import spray.json.JsValue
import spray.json.RootJsonFormat
import spray.json.deserializationError

object NetworkTypeFormat extends RootJsonFormat[NetworkType] {
  def write(networkType: NetworkType): JsValue = JsString(networkType.name)

  def read(value: JsValue): NetworkType = {
    value match {
      case name: JsString =>
        NetworkType.withName(name.value) match {
          case Some(networkType) => networkType
          case something => throw DeserializationException(s"Expected networkType instead of '$something'")
        }
      case _ => deserializationError("NetworkType expected")
    }
  }
}
