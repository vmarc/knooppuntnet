package kpn.api.common.route

case class SegmentRouteInfo(
  relationId: Long, // routeId in route tiles
  segmentIds: Seq[Long]
)
