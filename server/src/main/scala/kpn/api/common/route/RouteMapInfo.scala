package kpn.api.common.route

case class RouteMapInfo(
  segments: Seq[RouteSegment],
  paths: Seq[RoutePath],
)
