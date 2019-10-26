package kpn.core.database.views.location

import kpn.core.database.views.common.{View, ViewRow}
import kpn.shared.common.Ref
import spray.json.{DeserializationException, JsArray, JsNumber, JsString, JsValue}

object LocationView extends View {

  def convert(rowValue: JsValue): ViewRow = {
    toRow(rowValue)
  }

  def toRef(rowValue: JsValue): Ref = {
    val row = toRow(rowValue)
    row.value match {
      case JsArray(Vector(JsString(routeName), JsNumber(routeId))) => Ref(routeId.toLong, routeName)
      case _ =>
        throw DeserializationException("Expected route ref, but found: " + row.value.prettyPrint)
    }
  }

  override val reduce: Option[String] = Some("_count")
}
