package kpn.core.database.views.monitor

import kpn.api.common.monitor.MonitorGroup
import kpn.core.database.Database
import kpn.core.database.doc.MonitorGroupDoc
import kpn.core.database.doc.MonitorRouteDoc
import kpn.core.database.query.Query
import kpn.core.database.views.common.View
import kpn.server.api.monitor.domain.MonitorRoute

object MonitorRouteView extends View {

  private case class RouteViewResultRow(doc: MonitorRouteDoc)

  private case class RouteViewResult(rows: Seq[RouteViewResultRow])

  private case class GroupViewResultRow(doc: MonitorGroupDoc)

  private case class GroupViewResult(rows: Seq[GroupViewResultRow])

  def groups(database: Database, stale: Boolean = true): Seq[MonitorGroup] = {
    val query = Query(MonitorDesign, MonitorRouteView, classOf[GroupViewResult])
      .startKey(s"""["group"]""")
      .endKey(s"""["group", {}]""")
      .reduce(false)
      .includeDocs(true)
      .stale(stale)
    val result = database.execute(query)
    result.rows.map(_.doc.monitorGroup).sortBy(_.name)
  }

  def groupRoutes(database: Database, groupName: String, stale: Boolean = true): Seq[MonitorRoute] = {
    val query = Query(MonitorDesign, MonitorRouteView, classOf[RouteViewResult])
      .startKey(s"""["$groupName"]""")
      .endKey(s"""["$groupName", {}]""")
      .reduce(false)
      .includeDocs(true)
      .stale(stale)
    val result = database.execute(query)
    result.rows.map(_.doc.monitorRoute)
  }

  override val reduce: Option[String] = Some("_count")
}
