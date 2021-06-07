package kpn.core.database.views.changes

import kpn.api.common.ChangeSetSummary
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.Subset
import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.server.repository.QueryParameters

object ChangesViewChangesQuery {

  private case class ViewResultRowDoc(changeSetSummary: ChangeSetSummary)

  private case class ViewResultRow(doc: ViewResultRowDoc)

  private case class ViewResult(rows: Seq[ViewResultRow])

  def subsetChanges(database: Database, subset: Subset, parameters: ChangesParameters, stale: Boolean = true): Seq[ChangeSetSummary] = {
    val queryParameters = QueryParameters.subsetParametersFrom(subset, parameters)
    readChanges(database, queryParameters, stale)
  }

  def networkChanges(database: Database, networkId: Long, parameters: ChangesParameters, stale: Boolean = true): Seq[ChangeSetSummary] = {
    val queryParameters = QueryParameters.networkParametersFrom(networkId, parameters)
    readChanges(database, queryParameters, stale)
  }

  def routeChanges(database: Database, routeId: Long, parameters: ChangesParameters, stale: Boolean = true): Seq[ChangeSetSummary] = {
    val queryParameters = QueryParameters.routeParametersFrom(routeId, parameters)
    readChanges(database, queryParameters, stale)
  }

  def nodeChanges(database: Database, nodeId: Long, parameters: ChangesParameters, stale: Boolean = true): Seq[ChangeSetSummary] = {
    val queryParameters = QueryParameters.nodeParametersFrom(nodeId, parameters)
    readChanges(database, queryParameters, stale)
  }

  def changes(database: Database, parameters: ChangesParameters, stale: Boolean = true): Seq[ChangeSetSummary] = {
    val queryParameters = QueryParameters.parametersFrom(parameters)
    readChanges(database, queryParameters, stale)
  }

  private def readChanges(database: Database, queryParameters: Map[String, String], stale: Boolean = true): Seq[ChangeSetSummary] = {
    val query = Query(ChangesDesign, ChangesView, classOf[ViewResult])
      .startKey(queryParameters("startkey"))
      .endKey(queryParameters("endkey"))
      .skip(queryParameters("skip").toInt)
      .limit(queryParameters("limit").toInt)
      .includeDocs(true)
      .descending(true)
      .reduce(false)
      .stale(stale)

    val result = database.execute(query)
    result.rows.map(_.doc.changeSetSummary)
  }
}
