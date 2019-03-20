package kpn.core.repository

import kpn.core.planner.graph.GraphEdge
import kpn.shared.NetworkType

trait GraphRepository {
  def edges(networkType: NetworkType): Seq[GraphEdge]
}
