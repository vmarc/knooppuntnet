package kpn.core.database.views.poi

import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View
import kpn.core.poi.PoiInfo

object PoiTileView extends View {

  case class PoiViewResult(totalRows: Long, pois: Seq[PoiInfo])

  private case class ViewResultRow(
    key: Seq[String],
    value: Seq[String]
  )

  private case class ViewResult(rows: Seq[ViewResultRow])


  private case class TileViewResultRow(
    key: Seq[String]
  )

  private case class TileViewResult(rows: Seq[TileViewResultRow])

  def tilePoiInfos(tileName: String, database: Database, stale: Boolean = true): Seq[PoiInfo] = {
    val query = Query(PoiDesign, PoiTileView, classOf[ViewResult])
      .reduce(false)
      .startKey(s"""["$tileName"]""")
      .endKey(s"""["$tileName", {}]""")
      .stale(stale)

    val result = database.execute(query)

    result.rows.map { row =>
      val key = Fields(row.key)
      val value = Fields(row.value)
      PoiInfo(
        elementType = key.string(1),
        elementId = key.long(2),
        latitude = value.string(1),
        longitude = value.string(2),
        layer = value.string(0)
      )
    }
  }

  def allTiles(database: Database, stale: Boolean = true): Seq[String] = {

    val query = Query(PoiDesign, PoiTileView, classOf[TileViewResult])
      .reduce(true)
      .groupLevel(1)
      .stale(stale)

    val result = database.execute(query)

    result.rows.map { row =>
      val key = Fields(row.key)
      key.string(0)
    }
  }

  override val reduce: Option[String] = Some("_count")

}
