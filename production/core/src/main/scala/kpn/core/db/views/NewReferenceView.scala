package kpn.core.db.views

import kpn.core.db.json.JsonFormats.nodeNetworkReferenceFormat
import kpn.shared.node.NodeNetworkReference
import spray.json.JsValue

object NewReferenceView extends View {

  def convert(rowValue: JsValue): NodeNetworkReference = {
    val row = toRow(rowValue)
    nodeNetworkReferenceFormat.read(row.value)
  }

  override def reduce: Option[String] = None

}
