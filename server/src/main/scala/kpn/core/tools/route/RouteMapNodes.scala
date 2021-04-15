package kpn.core.tools.route

import kpn.api.common.route.RouteNetworkNodeInfo

case class RouteMapNodes(
  startNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  endNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  redundantNodes: Seq[RouteNetworkNodeInfo] = Seq.empty
)
