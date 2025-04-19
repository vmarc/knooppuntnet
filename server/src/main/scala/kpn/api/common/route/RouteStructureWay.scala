package kpn.api.common.route

import kpn.api.custom.Tag

case class RouteStructureWay(
  wayType: Option[String],
  nodes: Seq[RouteNetworkNodeInfo],
  accessible: Boolean,
  nodeCount: String,
  oneWay: WayDirection,
  oneWayTags: Seq[Tag],
)
