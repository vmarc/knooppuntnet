package kpn.core.database.views.monitor

import kpn.api.common.monitor.MonitorChangesParameters
import kpn.core.database.Database
import kpn.core.database.doc.MonitorRouteChangeDoc
import kpn.core.database.query.Query
import kpn.core.database.views.common.View
import kpn.server.api.monitor.domain.MonitorRouteChange

object MonitorChangesView extends View {

  private case class ViewResultRow(doc: MonitorRouteChangeDoc)

  private case class ViewResult(rows: Seq[ViewResultRow])

  def changes(database: Database, parameters: MonitorChangesParameters, stale: Boolean = true): Seq[MonitorRouteChange] = {
    val key = if (parameters.impact) """"impacted:change"""" else """"change""""
    doChanges(database, key, parameters, stale)
  }

  def groupChanges(database: Database, groupName: String, parameters: MonitorChangesParameters, stale: Boolean = true): Seq[MonitorRouteChange] = {
    val prefix = if (parameters.impact) "impacted:group" else "group"
    val key = s""""$prefix","$groupName""""
    doChanges(database, key, parameters, stale)
  }

  def routeChanges(database: Database, routeId: Long, parameters: MonitorChangesParameters, stale: Boolean = true): Seq[MonitorRouteChange] = {
    val prefix = if (parameters.impact) "impacted:route" else "route"
    val key = s""""$prefix",$routeId"""
    doChanges(database, key, parameters, stale)
  }

  private def doChanges(database: Database, key: String, parameters: MonitorChangesParameters, stale: Boolean = true): Seq[MonitorRouteChange] = {
    val skip = parameters.itemsPerPage * parameters.pageIndex
    val limit = parameters.itemsPerPage
    val query = Query(MonitorDesign, MonitorChangesView, classOf[ViewResult])
      .startKey(s"[$key,{}]")
      .endKey(s"[$key]")
      .reduce(false)
      .includeDocs(true)
      .descending(true)
      .skip(skip)
      .limit(limit)
      .stale(stale)
    val result = database.execute(query)
    result.rows.map(_.doc.monitorRouteChange)
  }

  override val reduce: Option[String] = Some("_count")
}
