package kpn.core.db.views

import kpn.core.poi.PoiInfo
import spray.json.DeserializationException
import spray.json.JsArray
import spray.json.JsNumber
import spray.json.JsString
import spray.json.JsValue

object PoiView extends View {

  def convert(rowValue: JsValue): PoiInfo = {
    val row = toRow(rowValue)
    row.key match {
      case JsArray(Vector(JsString(elementType), JsNumber(elementId), JsString(latitude), JsString(longitude), JsString(layer))) =>
        PoiInfo(elementType, elementId.toLong, latitude, longitude, layer)
      case _ => throw DeserializationException("expected array")
    }
  }

  override val map: String =
    """
      |function(doc) {
      |  var poi = doc.poi;
      |  if(poi) {
      |    var key = [
      |      poi.elementType,
      |      poi.elementId,
      |      poi.latitude,
      |      poi.longitude,
      |      poi.layers[0]
      |    ];
      |    emit(key, 1);
      |  }
      |}
    """.stripMargin

  override val reduce: Option[String] = Some("_sum")

}
