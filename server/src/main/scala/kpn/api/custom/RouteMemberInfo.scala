package kpn.api.custom

import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.common.route.WayDirection

case class RouteMemberInfo(
  id: Long,
  memberType: String,
  isWay: Boolean,
  nodes: Seq[RouteNetworkNodeInfo],
  linkName: String,
  from: String,
  fromNodeId: Long,
  to: String,
  toNodeId: Long,
  role: String,
  timestamp: Timestamp,
  isAccessible: Boolean,
  length: String,
  nodeCount: String,
  description: String,
  oneWay: WayDirection,
  oneWayTags: Tags
)
