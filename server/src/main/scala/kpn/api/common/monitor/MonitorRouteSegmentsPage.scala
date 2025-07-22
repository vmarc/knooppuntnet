package kpn.api.common.monitor

import kpn.api.common.route.SegmentInfo

case class MonitorRouteSegmentsPage(
  summary: MonitorRouteSummary,
  meters: Long,
  segments: Seq[SegmentInfo]
)
