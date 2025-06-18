package kpn.api.common.route

import kpn.api.common.Bounds
import kpn.api.common.RouteType

case class RouteSegmentData(
  name: String,
  routeTypes: Seq[RouteType],
  segments: Seq[RouteSegment],
  bounds: Option[Bounds],
)
