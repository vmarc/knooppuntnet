package kpn.core.database.views.changes

import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.server.repository.QueryParameters

object ChangesViewNodeChangesQuery {

  private case class ViewResultRowDoc(nodeChange: NodeChange)

  private case class ViewResultRow(doc: ViewResultRowDoc)

  private case class ViewResult(rows: Seq[ViewResultRow])

  def nodeChanges(database: Database, nodeId: Long, parameters: ChangesParameters, stale: Boolean = true): Seq[NodeChange] = {

    val queryParameters = QueryParameters.nodeParametersFrom(nodeId, parameters)

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
    result.rows.map(_.doc.nodeChange)
  }
}
