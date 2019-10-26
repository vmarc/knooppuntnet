package kpn.core.database.views.analyzer

import kpn.core.app.stats.Figure
import kpn.core.database.views.common.View
import spray.json.{DeserializationException, JsArray, JsNumber, JsString, JsValue}

object Overview extends View {

  def convert(rowValue: JsValue): Figure = {

    val row = toRow(rowValue)

    row.key match {
      case JsString(name) =>
        row.value match {
          case JsArray(
            Vector(
              JsNumber(nlRwn), JsNumber(nlRcn), JsNumber(nlRhn), JsNumber(nlRmn), JsNumber(nlRpn), JsNumber(nlRin),
              JsNumber(beRwn), JsNumber(beRcn), JsNumber(beRhn), JsNumber(beRmn), JsNumber(beRpn), JsNumber(beRin),
              JsNumber(deRwn), JsNumber(deRcn), JsNumber(deRhn), JsNumber(deRmn), JsNumber(deRpn), JsNumber(deRin),
              JsNumber(frRwn), JsNumber(frRcn), JsNumber(frRhn) // JsNumber(frRmn), JsNumber(frRpn), JsNumber(frRin)
              )
            ) =>
            Figure(
              name,
              nlRwn.toInt, nlRcn.toInt, nlRhn.toInt, nlRmn.toInt, nlRpn.toInt, nlRin.toInt,
              beRwn.toInt, beRcn.toInt, beRhn.toInt, beRmn.toInt, beRpn.toInt, beRin.toInt,
              deRwn.toInt, deRcn.toInt, deRhn.toInt, deRmn.toInt, deRpn.toInt, deRin.toInt,
              frRwn.toInt, frRcn.toInt, frRhn.toInt // frRmn.toInt, frRpn.toInt, frRin.toInt
            )
          case _ =>
            throw DeserializationException("Figure fields expected, but found:\n" + row.value.prettyPrint)
        }
      case _ => throw DeserializationException("string expected")
    }
  }
}
