package kpn.core.database.views.poi

import kpn.core.database.views.common.View

/*
  Provides an overview of points of interest that match multiple
  PoiDefintion's (= multiple layers).
 */
object PoiMultiLayerView extends View {

  override val map: String =
    """
      |function(doc) {
      |  var poi = doc.poi;
      |  if(poi && poi.layers && poi.layers.length > 1) {
      |    emit(poi.layers.join(), 1);
      |  }
      |}
    """.stripMargin

  override val reduce: Option[String] = Some("_sum")

}
