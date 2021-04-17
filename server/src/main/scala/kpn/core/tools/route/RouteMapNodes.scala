package kpn.core.tools.route

import kpn.api.common.route.RouteNetworkNodeInfo

case class RouteMapNodes(
  freeNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  startNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  endNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  redundantNodes: Seq[RouteNetworkNodeInfo] = Seq.empty
) {
  def isFreeRoute: Boolean = freeNodes.nonEmpty && startNodes.isEmpty && endNodes.isEmpty
}
