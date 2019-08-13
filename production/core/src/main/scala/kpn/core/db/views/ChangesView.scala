package kpn.core.db.views

import kpn.shared.changes.filter.ChangesFilterPeriod
import spray.json.DeserializationException
import spray.json.JsArray
import spray.json.JsNumber
import spray.json.JsString
import spray.json.JsValue

object ChangesView extends View {

  def convertToPeriod(rowValue: JsValue): ChangesFilterPeriod = {
    val row = toRow(rowValue)
    val periodName = row.key match {
      case JsArray(values) =>
        values.last match {
          case JsString(name) => name
          case _ => throw DeserializationException("Expected string")
        }
      case _ => throw DeserializationException("Array expected")
    }
    row.value match {
      case JsArray(Vector(JsNumber(total), JsNumber(impacted))) => ChangesFilterPeriod(periodName, total.toInt, impacted.toInt)
      case _ => throw DeserializationException("Total and impacted counts expected")
    }
  }

  def extractTotal(rowValue: JsValue): Int = {
    val row = toRow(rowValue)
    row.value match {
      case JsArray(Vector(JsNumber(total), JsNumber(impacted))) => total.toInt
      case _ => throw DeserializationException("Total and impacted counts expected")
    }
  }

  override val reduce: Option[String] = Some("_sum")
}
