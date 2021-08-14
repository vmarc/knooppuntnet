package kpn.core.database.views.monitor

import kpn.api.common.monitor.MonitorGroup
import kpn.core.database.Database
import kpn.core.database.doc.MonitorGroupDoc
import kpn.core.database.doc.MonitorRouteDoc
import kpn.core.database.views.common.View
import kpn.server.api.monitor.domain.MonitorRoute

object MonitorRouteView extends View {

  private case class RouteIdViewResultRow(key: Seq[String])

  private case class RouteIdViewResult(rows: Seq[RouteIdViewResultRow])

  private case class RouteViewResultRow(doc: MonitorRouteDoc)

  private case class RouteViewResult(rows: Seq[RouteViewResultRow])

  private case class GroupViewResultRow(doc: MonitorGroupDoc)

  private case class GroupViewResult(rows: Seq[GroupViewResultRow])

  def allRouteIds(database: Database): Seq[Long] = {
    Seq.empty
  }

  def groups(database: Database): Seq[MonitorGroup] = {
    Seq.empty
  }

  def groupRoutes(database: Database, groupName: String): Seq[MonitorRoute] = {
    Seq.empty
  }

  def routes(database: Database): Seq[MonitorRoute] = {
    Seq.empty
  }

  override val reduce: Option[String] = Some("_count")
}
