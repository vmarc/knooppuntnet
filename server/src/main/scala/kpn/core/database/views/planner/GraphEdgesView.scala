package kpn.core.database.views.planner

import kpn.core.database.views.common.View
import kpn.core.planner.graph.GraphEdge
import kpn.shared.common.TrackPathKey
import spray.json.JsArray
import spray.json.JsNumber
import spray.json.JsString
import spray.json.JsValue

/**
  * View to derive graph edges from routes (to be used for routing).
  */
object GraphEdgesView extends View {

  def convert(row: JsValue): GraphEdge = {
    val key = row.asJsObject.getFields("key").head
    val value = row.asJsObject.getFields("value").head
    key match {
      case JsArray(Vector(JsString(networkType), JsNumber(routeId), JsString(pathType), JsNumber(pathIndex))) =>
        value match {
          case JsArray(Vector(JsNumber(startNodeId), JsNumber(endNodeId), JsNumber(meters))) =>
            GraphEdge(startNodeId.longValue(), endNodeId.longValue(), meters.intValue(), TrackPathKey(routeId.longValue(), pathType, pathIndex.intValue()))
        }
    }
  }

  override val reduce: Option[String] = Some("_count")
}
