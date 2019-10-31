package kpn.core.database.views.poi

import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View
import kpn.core.poi.PoiInfo

object PoiView extends View {

  case class PoiViewResult(totalRows: Int, pois: Seq[PoiInfo])

  private case class ViewResultRow(
    id: String,
    key: Seq[String],
    value: Int
  )

  private case class ViewResult(total_rows: Int, offset: Int, rows: Seq[ViewResultRow])

  def query(database: Database, limit: Int, skip: Int, stale: Boolean = true): PoiViewResult = {

    val query = Query(PoiDesign, PoiView, classOf[ViewResult])
      .reduce(false)
      .limit(limit)
      .skip(skip)

    val result = database.execute(query)

    val pois = result.rows.map { row =>
      val key = Fields(row.key)
      PoiInfo(
        elementType = key.string(0),
        elementId = key.long(1),
        latitude = key.string(2),
        longitude = key.string(3),
        layer = key.string(4)
      )
    }
    PoiViewResult(result.total_rows, pois)
  }

  override val map: String =
    """
      |function(doc) {
      |  var poi = doc.poi;
      |  if(poi) {
      |    if (!!poi.layers && poi.layers.length > 0) {
      |      var key = [
      |        poi.elementType,
      |        poi.elementId,
      |        poi.latitude,
      |        poi.longitude,
      |        poi.layers[0]
      |      ];
      |      emit(key, 1);
      |    }
      |  }
      |}
    """.stripMargin

  override val reduce: Option[String] = Some("_sum")

}
