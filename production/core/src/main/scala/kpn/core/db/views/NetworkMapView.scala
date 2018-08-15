package kpn.core.db.views

import kpn.core.db.json.JsonFormats.networkShapeFormat
import kpn.shared.network.NetworkMapInfo
import spray.json.JsArray
import spray.json.JsNumber
import spray.json.JsString
import spray.json.JsValue

object NetworkMapView extends View {

  def convert(rowValue: JsValue): NetworkMapInfo = {
    val row = toRow(rowValue)
    row.key match {
      case JsArray(Vector(JsString(networkCountry), JsString(networkType), JsString(networkName), JsNumber(networkId))) =>
        val shape = networkShapeFormat.read(row.value)
        NetworkMapInfo(networkId.toLong, networkName, shape)
    }
  }

  override val reduce: Option[String] = None
}
