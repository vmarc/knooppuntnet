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

  private case class CountViewResultRow(value: Long)

  private case class CountViewResult(rows: Seq[CountViewResultRow])

  def changes(database: Database, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    val key = if (parameters.impact) """"impacted:change"""" else """"change""""
    doChanges(database, key, parameters)
  }

  def changesCount(database: Database, parameters: MonitorChangesParameters): Long = {
    val key = if (parameters.impact) """"impacted:change"""" else """"change""""
    doChangesCount(database, key, parameters)
  }

  def groupChanges(database: Database, groupName: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    val prefix = if (parameters.impact) "impacted:group" else "group"
    val key = s""""$prefix","$groupName""""
    doChanges(database, key, parameters)
  }

  def groupChangesCount(database: Database, groupName: String, parameters: MonitorChangesParameters): Long = {
    val prefix = if (parameters.impact) "impacted:group" else "group"
    val key = s""""$prefix","$groupName""""
    doChangesCount(database, key, parameters)
  }

  def routeChanges(database: Database, routeId: Long, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    val prefix = if (parameters.impact) "impacted:route" else "route"
    val key = s""""$prefix",$routeId"""
    doChanges(database, key, parameters)
  }

  def routeChangesCount(database: Database, routeId: Long, parameters: MonitorChangesParameters): Long = {
    val prefix = if (parameters.impact) "impacted:route" else "route"
    val key = s""""$prefix",$routeId"""
    doChangesCount(database, key, parameters)
  }

  private def doChanges(database: Database, key: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
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
    val result = database.execute(query)
    result.rows.map(_.doc.monitorRouteChange)
  }

  private def doChangesCount(database: Database, key: String, parameters: MonitorChangesParameters): Long = {
    val query = Query(MonitorDesign, MonitorChangesView, classOf[CountViewResult])
      .startKey(s"[$key]")
      .endKey(s"[$key,{}]")
      .reduce(true)
    val result = database.execute(query)
    result.rows.map(_.value).sum
  }

  override val reduce: Option[String] = Some("_count")
}
