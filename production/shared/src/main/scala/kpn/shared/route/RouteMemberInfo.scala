package kpn.shared.route

import kpn.shared.Timestamp
import kpn.shared.data.Tags

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
