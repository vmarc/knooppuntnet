package kpn.core.database.views.changes

import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.server.repository.QueryParameters
import kpn.shared.changes.details.RouteChange
import kpn.shared.changes.filter.ChangesParameters

object ChangesViewRouteChangesQuery {

  private case class ViewResultRowDoc(routeChange: RouteChange)

  private case class ViewResultRow(doc: ViewResultRowDoc)

  private case class ViewResult(rows: Seq[ViewResultRow])

  def routeChanges(database: Database, parameters: ChangesParameters, stale: Boolean = true): Seq[RouteChange] = {

    val queryParameters = QueryParameters.from(parameters)

    val query = Query(ChangesDesign, ChangesView, classOf[ViewResult])
      .startKey(queryParameters("startkey"))
      .endKey(queryParameters("endkey"))
      .skip(queryParameters("skip").toInt)
      .limit(queryParameters("limit").toInt)
      .includeDocs(true)
      .descending(true)
      .reduce(false)

    val result = database.execute(query)
    result.rows.map(_.doc.routeChange)
  }

}
