package kpn.server.repository

import kpn.core.database.Database
import kpn.core.database.views.planner.GraphEdgesView
import kpn.core.db.couch.Couch
import kpn.core.planner.graph.GraphEdge
import kpn.shared.NetworkType
import org.springframework.stereotype.Component
import spray.http.Uri
import spray.http.Uri.Query

@Component
class GraphRepositoryImpl(analysisDatabase: Database) extends GraphRepository {

  override def edges(networkType: NetworkType): Seq[GraphEdge] = {

    val uri = Uri(s"_design/PlannerDesign/_view/GraphEdgesView?reduce=false&stale=ok&startkey=[${networkType.name}]&endkey=[${networkType.name},{}]")

    val parameters = Map(
      "startkey" -> s"""["${networkType.name}"]""",
      "endkey" -> s"""["${networkType.name}",{}]""",
      "reduce" -> "false",
      "stale" -> "ok"
    )

    val request = uri.withQuery(Query(parameters))
    analysisDatabase.old.getRows(request.toString(), Couch.batchTimeout).map(GraphEdgesView.convert)
  }
}
