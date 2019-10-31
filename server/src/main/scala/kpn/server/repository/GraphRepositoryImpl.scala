package kpn.server.repository

import kpn.core.database.Database
import kpn.core.database.views.planner.GraphEdgesView
import kpn.core.planner.graph.GraphEdge
import kpn.shared.NetworkType
import org.springframework.stereotype.Component

@Component
class GraphRepositoryImpl(analysisDatabase: Database) extends GraphRepository {

  override def edges(networkType: NetworkType): Seq[GraphEdge] = {
    GraphEdgesView.query(analysisDatabase, networkType, stale = false)
  }
}
