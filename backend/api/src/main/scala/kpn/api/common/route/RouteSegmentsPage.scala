package kpn.api.common.route

case class RouteSegmentsPage(
  routeInfo: RouteInfo,
  segments: Seq[SegmentInfo],
)
