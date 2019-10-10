package kpn.core.repository

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.views.GraphEdgesView
import kpn.core.planner.graph.GraphEdge
import kpn.shared.NetworkType
import spray.http.Uri
import spray.http.Uri.Query

class GraphRepositoryImpl(database: Database) extends GraphRepository {

  override def edges(networkType: NetworkType): Seq[GraphEdge] = {

    val uri = Uri(s"_design/PlannerDesign/_view/GraphEdgesView?reduce=false&stale=ok&startkey=[${networkType.name}]&endkey=[${networkType.name},{}]")

    val parameters = Map(
      "startkey" -> s"""["${networkType.name}"]""",
      "endkey" -> s"""["${networkType.name}",{}]""",
      "reduce" -> "false",
      "stale" -> "ok"
    )

    val request = uri.withQuery(Query(parameters))
    database.getRows(request.toString(), Couch.batchTimeout).map(GraphEdgesView.convert)
  }
}
