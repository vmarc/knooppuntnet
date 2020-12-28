package kpn.core.database.views.monitor

import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object MonitorRouteReferenceView extends View {

  private case class ViewResultRow(value: String)

  private case class ViewResult(rows: Seq[ViewResultRow])

  def reference(database: Database, routeId: Long, stale: Boolean = true): Option[String] = {
    val query = Query(MonitorDesign, MonitorRouteReferenceView, classOf[ViewResult])
      .keyLong(routeId)
      .reduce(false)
      .stale(stale)
    val result = database.execute(query)
    if (result.rows.size == 1) {
      Some(result.rows.head.value)
    }
    else {
      None
    }
  }

  override val reduce: Option[String] = Some("_count")
}
