package kpn.api.common.monitor

import kpn.api.common.route.RouteSegment
import kpn.api.common.route.SuperSegment

case class MonitorRouteSegmentsPage(
  summary: MonitorRouteSummary,
  meters: Long,
  segments: Seq[RouteSegment],
  superDistance: Long,
  superSegments: Seq[SuperSegment]
)
