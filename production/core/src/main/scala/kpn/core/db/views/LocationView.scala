package kpn.core.db.views

import spray.json.JsValue

object LocationView extends View {

  def convert(rowValue: JsValue): ViewRow = {
    toRow(rowValue)
  }

  override val reduce: Option[String] = Some("_count")
}
