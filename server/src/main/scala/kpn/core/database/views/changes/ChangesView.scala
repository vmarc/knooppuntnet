package kpn.core.database.views.changes

import kpn.api.common.ChangeSetSummary
import kpn.api.common.changes.details.LocationChange
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilterPeriod
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.Subset
import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object ChangesView extends View {

  private case class ViewResultRow(key: Seq[String], value: Seq[Long])

  private case class ViewResult(rows: Seq[ViewResultRow])

  def queryPeriod(database: Database, suffixLength: Int, keys: Seq[String], stale: Boolean = true): Seq[ChangesFilterPeriod] = {

    def keyString(values: Seq[String]): String = values.mkString("[\"", "\", \"", "\"]")

    def suffix(size: Int, character: String): String = Seq.fill(size)(character).mkString

    val startKey = keyString(keys :+ suffix(suffixLength, "9"))
    val endKey = keyString(keys :+ suffix(suffixLength, "0"))

    val query = Query(ChangesDesign, ChangesView, classOf[ViewResult])
      .startKey(startKey)
      .endKey(endKey)
      .reduce(true)
      .descending(true)
      .groupLevel(keys.size + 1)
      .stale(stale)

    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      ChangesFilterPeriod(
        name = key.string(keys.size),
        totalCount = row.value.head,
        impactedCount = row.value(1)
      )
    }
  }

  def queryChangeCount(database: Database, elementType: String, elementId: Long, stale: Boolean = true): Long = {
    val query = Query(ChangesDesign, ChangesView, classOf[ViewResult])
      .keyStartsWith(elementType, elementId.toString)
      .groupLevel(2)
      .stale(stale)
    val result = database.execute(query)
    result.rows.map(_.value.head).sum
  }

  def changes(database: Database, parameters: ChangesParameters, stale: Boolean = true): Seq[ChangeSetSummary] = {
    ChangesViewChangesQuery.changes(database, parameters, stale)
  }

  def subsetChanges(database: Database, subset: Subset, parameters: ChangesParameters, stale: Boolean = true): Seq[ChangeSetSummary] = {
    ChangesViewChangesQuery.subsetChanges(database, subset, parameters, stale)
  }

  def locationChanges(database: Database, location: String, parameters: ChangesParameters, stale: Boolean = true): Seq[LocationChange] = {
    ChangesViewLocationChangesQuery.locationChanges(database, location: String, parameters, stale)
  }

  def networkChanges(database: Database, networkId: Long, parameters: ChangesParameters, stale: Boolean = true): Seq[NetworkInfoChange] = {
    ChangesViewNetworkChangesQuery.networkChanges(database, networkId, parameters, stale)
  }

  def routeChanges(database: Database, routeId: Long, parameters: ChangesParameters, stale: Boolean = true): Seq[RouteChange] = {
    ChangesViewRouteChangesQuery.routeChanges(database, routeId, parameters, stale)
  }

  def nodeChanges(database: Database, nodeId: Long, parameters: ChangesParameters, stale: Boolean = true): Seq[NodeChange] = {
    ChangesViewNodeChangesQuery.nodeChanges(database, nodeId, parameters, stale)
  }

  override val reduce: Option[String] = Some("_sum")
}
