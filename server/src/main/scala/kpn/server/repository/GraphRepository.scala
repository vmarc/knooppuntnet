package kpn.server.repository

import kpn.api.common.NetworkType
import kpn.core.planner.graph.NodeNetworkGraph

trait GraphRepository {
  def graph(networkType: NetworkType): Option[NodeNetworkGraph]
}
