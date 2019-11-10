package kpn.server.repository

import kpn.api.custom.NetworkType
import kpn.core.planner.graph.GraphEdge

trait GraphRepository {
  def edges(networkType: NetworkType): Seq[GraphEdge]
}
