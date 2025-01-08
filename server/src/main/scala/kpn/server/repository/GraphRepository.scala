package kpn.server.repository

import kpn.api.common.RouteType
import kpn.core.planner.graph.NodeNetworkGraph

trait GraphRepository {
  def graph(routeType: RouteType): Option[NodeNetworkGraph]
}
