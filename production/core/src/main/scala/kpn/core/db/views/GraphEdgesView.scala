package kpn.core.db.views

import spray.json.JsArray
import spray.json.JsNumber
import spray.json.JsString
import spray.json.JsValue

/**
  * View to derive graph edges from routes (to be used for routing).
  */
object GraphEdgesView extends View {

  case class Row(networkType: String, routeId: Long, pathType: String, pathIndex: Int, startNodeId: Long, endNodeId: Long, meters: Int)

  def convert(row: JsValue): Row = {
    val key = row.asJsObject.getFields("key").head
    val value = row.asJsObject.getFields("value").head
    key match {
      case JsArray(Vector(JsString(networkType), JsNumber(routeId), JsString(pathType), JsNumber(pathIndex))) =>
        value match {
          case JsArray(Vector(JsNumber(startNodeId), JsNumber(endNodeId), JsNumber(meters))) =>
            Row(networkType, routeId.longValue(), pathType, pathIndex.intValue(), startNodeId.longValue(), endNodeId.longValue(), meters.intValue())
        }
    }
  }

  override val reduce: Option[String] = Some("_count")
}
