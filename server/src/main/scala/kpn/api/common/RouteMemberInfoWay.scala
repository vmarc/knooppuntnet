package kpn.api.common

import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.common.route.WayDirection
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class RouteMemberInfoWay(
  nodes: Seq[RouteNetworkNodeInfo],
  from: String,
  fromNodeId: Long,
  to: String,
  toNodeId: Long,
  timestamp: Timestamp,
  accessible: Boolean,
  distance: Long,
  nodeCount: String,
  description: String,
  oneWay: WayDirection,
  oneWayTags: Seq[Tag]
)
