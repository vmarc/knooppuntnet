package kpn.core.db.views

import spray.json.JsNumber
import spray.json.JsString
import spray.json.JsValue

object DocumentView extends View {

  case class DocumentCount(prefix: String, count: Int)

  def convert(rowValue: JsValue): DocumentCount = {

    val row = toRow(rowValue)
    val key = row.key match {
      case JsString(string) => string
      case _ => "?"
    }
    val count = row.value match {
      case JsNumber(number) => number.toInt
      case _ => 0
    }

    DocumentCount(key, count)
  }

  override val map: String =
    """
      |function(doc) {
      |	 var prefix = doc._id.substring(0, doc._id.indexOf(':'));
      |  emit(prefix, 1);
      |}
    """.stripMargin

  override val reduce: Option[String] = Some("_sum")
}
