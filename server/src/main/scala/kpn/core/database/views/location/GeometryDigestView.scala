package kpn.core.database.views.location

import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object GeometryDigestView extends View {

  private case class ViewResultRow(value: String)

  private case class ViewResult(rows: Seq[ViewResultRow])

  def query(database: Database, routeId: Long, stale: Boolean = true): Option[String] = {

    val query = Query(LocationDesign, GeometryDigestView, classOf[ViewResult])
      .keyLong(routeId)
      .stale(stale)

    val result = database.execute(query)
    result.rows.headOption.map(_.value)
  }

  override val reduce: Option[String] = None
}
