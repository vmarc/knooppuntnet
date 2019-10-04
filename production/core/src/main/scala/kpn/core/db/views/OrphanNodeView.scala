package kpn.core.db.views

import kpn.core.db.json.JsonFormats.nodeInfoFormat
import kpn.shared.NodeInfo
import spray.json.DeserializationException
import spray.json.JsArray
import spray.json.JsBoolean
import spray.json.JsNumber
import spray.json.JsString
import spray.json.JsValue

object OrphanNodeView extends View {

  def main(args: Array[String]): Unit = {
    println(OrphanNodeView.map)
  }

  case class OrphanNodeKey(orphan: Boolean, display: Boolean, country: String, networkType: String, id: Long)

  def toKeyAndValue(rowValue: JsValue): (OrphanNodeKey, NodeInfo) = {
    val row = toRow(rowValue)
    val key = row.key match {
      case JsArray(Vector(JsBoolean(orphan), JsBoolean(display), JsString(country), JsString(networkType), JsNumber(id))) =>
        OrphanNodeKey(orphan, display, country, networkType, id.toLong)
      case _ =>
        throw DeserializationException("key structure expected")
    }
    val value = nodeInfoFormat.read(row.value)
    (key, value)
  }

  def convert(rowValue: JsValue): NodeInfo = {
    val row = toRow(rowValue)
    nodeInfoFormat.read(row.value)
  }

  override val reduce: Option[String] = Some("_count")
}
