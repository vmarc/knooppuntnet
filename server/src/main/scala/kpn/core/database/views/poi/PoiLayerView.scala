package kpn.core.database.views.poi

import kpn.core.database.views.common.View

object PoiLayerView extends View {

  override val map: String =
    """
      |function(doc) {
      |  var poi = doc.poi;
      |  if(poi) {
      |    var key = [
      |      poi.layers[0],
      |      poi.elementType,
      |      poi.elementId
      |    ];
      |    emit(key, 1);
      |  }
      |}
    """.stripMargin

  override val reduce: Option[String] = Some("_sum")

}
