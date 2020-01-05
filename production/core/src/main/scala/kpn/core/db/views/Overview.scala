package kpn.core.db.views

import kpn.core.app.stats.Figure
import spray.json.DeserializationException
import spray.json.JsArray
import spray.json.JsNumber
import spray.json.JsString
import spray.json.JsValue

object Overview extends View {

  def convert(rowValue: JsValue): Figure = {

    val row = toRow(rowValue)

    row.key match {
      case JsString(name) =>
        row.value match {
          case numbers: JsArray =>
            Figure(
              name,
              figure(numbers, 0),
              figure(numbers, 1),
              figure(numbers, 2),
              figure(numbers, 3),
              figure(numbers, 4),
              figure(numbers, 5),
              figure(numbers, 6),
              figure(numbers, 7),
              figure(numbers, 8),
              figure(numbers, 9),
              figure(numbers, 10),
              figure(numbers, 11),
              figure(numbers, 12),
              figure(numbers, 13),
              figure(numbers, 14),
              figure(numbers, 15),
              figure(numbers, 16),
              figure(numbers, 17),
              figure(numbers, 18),
              figure(numbers, 19),
              figure(numbers, 20),
              figure(numbers, 21),
              figure(numbers, 22),
              figure(numbers, 23),
              figure(numbers, 24),
              figure(numbers, 25),
              figure(numbers, 26),
              figure(numbers, 27),
              figure(numbers, 28),
              figure(numbers, 29)
            )
          case _ =>
            throw DeserializationException("Figure fields expected, but found:\n" + row.value.prettyPrint)
        }
      case _ => throw DeserializationException("string expected")
    }
  }

  private def figure(numbers: JsArray, index: Int): Int = {
    numbers.elements(index).asInstanceOf[JsNumber].value.toInt
  }
}
