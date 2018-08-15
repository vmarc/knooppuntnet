package kpn.core.db.json

import kpn.core.common.TimestampUtil
import kpn.shared.Timestamp
import spray.json.JsString
import spray.json.JsValue
import spray.json.RootJsonFormat
import spray.json.deserializationError

object TimestampFormat extends RootJsonFormat[Timestamp] {

  def write(timestamp: Timestamp): JsValue = JsString(timestamp.iso)

  def read(value: JsValue): Timestamp = {
    value match {
      case jsString: JsString => TimestampUtil.parseIso(jsString.value)
      case _ => deserializationError("Timestamp expected")
    }
  }
}
