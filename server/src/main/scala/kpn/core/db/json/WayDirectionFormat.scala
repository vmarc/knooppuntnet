package kpn.core.db.json

import kpn.shared.route.Backward
import kpn.shared.route.Both
import kpn.shared.route.Forward
import kpn.shared.route.WayDirection
import spray.json.DeserializationException
import spray.json.JsString
import spray.json.JsValue
import spray.json.RootJsonFormat

object WayDirectionFormat extends RootJsonFormat[WayDirection] {

  def write(obj: WayDirection): JsValue = {
    JsString(obj.toString)
  }

  def read(json: JsValue): WayDirection = {
    json match {
      case JsString("Both") => Both
      case JsString("Forward") => Forward
      case JsString("Backward") => Backward
      case something => throw DeserializationException(s"Expected a value Both|forward|Backward instead of $something")
    }
  }
}
