package kpn.server.repository

import kpn.api.custom.NetworkType
import kpn.core.planner.graph.NodeNetworkGraph

trait GraphRepository {
  def graph(networkType: NetworkType): Option[NodeNetworkGraph]
}
