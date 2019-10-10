package kpn.core.db.views

import kpn.core.db.json.JsonFormats.networkAttributesFormat
import kpn.shared.network.NetworkAttributes
import spray.json.JsValue

/**
  * Database view to pick up network attributes by country and network type.
  *
  * See also: NetworkRepositoryImpl and NetworkRepositoryTest.
  */
object NetworkView extends View {

  def convert(rowValue: JsValue): NetworkAttributes = {
    val row = toRow(rowValue)
    networkAttributesFormat.read(row.value)
  }

  override val reduce: Option[String] = None
}
