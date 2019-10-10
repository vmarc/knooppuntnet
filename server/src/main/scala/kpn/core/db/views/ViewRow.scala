package kpn.core.db.views

import spray.json.JsNull
import spray.json.JsValue

class ViewRow(row: JsValue) {
  private val rowObject = row.asJsObject
  val id: JsValue = {
    val fields = rowObject.getFields("id")
    if (fields.isEmpty) {
      JsNull
    }
    else fields.head
  }
  val key: JsValue = rowObject.getFields("key").head
  val value: JsValue = rowObject.getFields("value").head
}
