package kpn.api.common.monitor

import kpn.api.common.route.SegmentInfo

case class MonitorRouteSegmentsPage(
  summary: MonitorRouteSummary,
  meters: Long,
  relations: Seq[MonitorRouteRelationInfo],
  segments: Seq[SegmentInfo]
)
