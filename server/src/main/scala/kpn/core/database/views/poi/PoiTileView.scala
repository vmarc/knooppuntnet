package kpn.core.database.views.poi

import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View
import kpn.core.poi.PoiInfo
import kpn.server.analyzer.engine.poi.PoiRef

object PoiTileView extends View {

  case class PoiViewResult(totalRows: Long, pois: Seq[PoiInfo])

  private case class ViewResultRow(
    key: Seq[String]
  )

  private case class ViewResult(rows: Seq[ViewResultRow])

  def tilePoiRefs(tileName: String, database: Database, stale: Boolean = true): Seq[PoiRef] = {
    val query = Query(PoiDesign, PoiTileView, classOf[ViewResult])
      .reduce(false)
      .startKey(s"""["$tileName"]""")
      .endKey(s"""["$tileName", {}]""")
      .stale(stale)

    val result = database.execute(query)

    result.rows.map { row =>
      val key = Fields(row.key)
      PoiRef(
        key.string(1),
        key.long(2),
      )
    }
  }

  def allTiles(database: Database, stale: Boolean = true): Seq[String] = {

    val query = Query(PoiDesign, PoiTileView, classOf[ViewResult])
      .reduce(true)
      .groupLevel(1)
      .stale(stale)

    val result = database.execute(query)

    result.rows.map { row =>
      val key = Fields(row.key)
      key.string(0)
    }
  }

  override val map: String =
    """
      |function(doc) {
      |  var poi = doc.poi;
      |  if(poi) {
      |    if (!!poi.tiles) {
      |      for (var i = 0; i < poi.tiles.length; i++) {
      |        var tile = poi.tiles[i];
      |        var key = [
      |          tile,
      |          poi.elementType,
      |          poi.elementId
      |        ];
      |        emit(key, 1);
      |      }
      |    }
      |  }
      |}
    """.stripMargin

  override val reduce: Option[String] = Some("_sum")

}
