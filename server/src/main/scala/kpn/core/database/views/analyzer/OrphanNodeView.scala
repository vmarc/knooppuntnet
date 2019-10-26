package kpn.core.database.views.analyzer

import kpn.core.database.views.common.View
import kpn.core.db.json.JsonFormats.nodeInfoFormat
import kpn.shared.NodeInfo
import spray.json.{DeserializationException, JsArray, JsBoolean, JsNumber, JsString, JsValue}

object OrphanNodeView extends View {

  def main(args: Array[String]): Unit = {
    println(OrphanNodeView.map)
  }

  case class OrphanNodeKey(orphan: Boolean, country: String, networkType: String, id: Long)

  def toKeyAndValue(rowValue: JsValue): (OrphanNodeKey, NodeInfo) = {
    val row = toRow(rowValue)
    val key = row.key match {
      case JsArray(Vector(JsBoolean(orphan), JsString(country), JsString(networkType), JsNumber(id))) =>
        OrphanNodeKey(orphan, country, networkType, id.toLong)
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
