package kpn.core.database.views.tile

import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object TileView extends View {

  private case class ViewResultRow(key: Seq[String])

  private case class ViewResult(rows: Seq[ViewResultRow])

  def nodeIds(database: Database, fullTileName: String): Seq[Long] = {
    query(database, fullTileName, "node")
  }

  def routeIds(database: Database, fullTileName: String): Seq[Long] = {
    query(database, fullTileName, "route")
  }

  def query(database: Database, fullTileName: String, elementType: String): Seq[Long] = {

    val query = Query(TileDesign, TileView, classOf[ViewResult])
      .args(Seq(fullTileName, elementType))
      .reduce(false)

    val result = database.execute(query)
    result.rows.map(row => Fields(row.key).long(2))
  }

  override val reduce: Option[String] = Some("_count")
}
