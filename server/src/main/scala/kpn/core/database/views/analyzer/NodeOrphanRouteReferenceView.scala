package kpn.core.database.views.analyzer

import kpn.core.database.views.common.View
import kpn.core.db.json.JsonFormats.nodeOrphanRouteReferenceFormat
import kpn.shared.node.NodeOrphanRouteReference
import spray.json.JsValue

object NodeOrphanRouteReferenceView extends View {

  def convert(rowValue: JsValue): NodeOrphanRouteReference = {
    val row = toRow(rowValue)
    nodeOrphanRouteReferenceFormat.read(row.value)
  }

  override def reduce: Option[String] = None

}
