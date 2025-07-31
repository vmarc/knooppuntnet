package kpn.api.common.route

import kpn.api.common.Bounds
import kpn.api.common.RouteType

case class RouteInfo(
  routeId: Long,
  routeName: String,
  routeTypes: Seq[RouteType],
  memberCount: Long,
  pathCount: Long,
  segmentCount: Long,
  changeCount: Long,
  bounds: Option[Bounds],
)
