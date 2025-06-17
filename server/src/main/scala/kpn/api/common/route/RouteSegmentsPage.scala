package kpn.api.common.route

case class RouteSegmentsPage(
  data: RouteSegmentData,
  changeCount: Long,
  segmentCount: Long,
)
