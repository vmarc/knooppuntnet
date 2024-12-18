package kpn.api.common.route

import kpn.api.common.data.MemberType
import kpn.api.custom.Day
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class RouteStructureRow(
  // RouteMemberInfo
  id: Long,
  memberType: MemberType,
  isWay: Boolean,
  nodes: Seq[RouteNetworkNodeInfo],
  linkName: String,
  from: String,
  fromNodeId: Long,
  to: String,
  toNodeId: Long,
  role: String,
  timestamp: Timestamp,
  accessible: Boolean,
  length: String,
  nodeCount: String,
  description: String,
  oneWay: WayDirection,
  oneWayTags: Seq[Tag],

  // MonitorRouteRelationStructureRow
  level: Long,
  physical: Boolean,
  name: String,
  relationId: Long,
  subRelationIndex: Option[Long],
  // role: Option[String],
  survey: Option[Day],
  symbol: Option[String],
  osmSegmentCount: Option[Long],
  osmDistance: Long,
  gaps: Option[String],
  happy: Boolean
)
