package kpn.api.common.route

import kpn.api.common.Bounds

case class SegmentInfo(
  id: Long,
  meters: Long,
  bounds: Option[Bounds],
  routeInfos: Seq[SegmentRouteInfo]
)
