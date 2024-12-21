package kpn.api.common.route

import kpn.api.custom.Tag

case class RouteStructureWay(
  nodes: Seq[RouteNetworkNodeInfo],
  from: String,
  fromNodeId: Long,
  to: String,
  toNodeId: Long,
  accessible: Boolean,
  nodeCount: String,
  description: String,
  oneWay: WayDirection,
  oneWayTags: Seq[Tag],
)
