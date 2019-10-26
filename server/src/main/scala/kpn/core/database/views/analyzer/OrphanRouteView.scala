package kpn.core.database.views.analyzer

import kpn.core.database.views.common.View
import kpn.core.db.json.JsonFormats.routeSummaryFormat
import kpn.shared.RouteSummary
import spray.json.{DeserializationException, JsArray, JsBoolean, JsNumber, JsString, JsValue}

object OrphanRouteView extends View {

  case class OrphanRouteKey(orphan: Boolean, country: String, networkType: String, id: Long)

  def toKeyAndValue(rowValue: JsValue): (OrphanRouteKey, RouteSummary) = {
    val row = toRow(rowValue)
    val key = row.key match {
      case JsArray(Vector(JsBoolean(orphan), JsString(country), JsString(networkType), JsNumber(id))) =>
        OrphanRouteKey(orphan, country, networkType, id.toLong)
      case _ =>
        throw DeserializationException("key structure expected")
    }
    val value = routeSummaryFormat.read(row.value)
    (key, value)
  }

  def convert(rowValue: JsValue): RouteSummary = {
    val row = toRow(rowValue)
    routeSummaryFormat.read(row.value)
  }

  def toObjectId(rowValue: JsValue): Long = {
    val rowObject = rowValue.asJsObject
    rowObject.getFields("key").head match {
      case JsArray(values) =>
        values(5) match {
          case JsNumber(id) => id.toLong
          case _ => throw DeserializationException("expected number")
        }
      case _ => throw DeserializationException("expected key array")
    }
  }

  override val reduce: Option[String] = Some("_count")
}
