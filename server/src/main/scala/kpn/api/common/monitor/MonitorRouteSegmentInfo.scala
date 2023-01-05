package kpn.api.common.monitor

import kpn.api.common.Bounds

case class MonitorRouteSegmentInfo(
  id: Long,
  relationId: Long,
  osmSegmentId: Long,
  startNodeId: Long,
  endNodeId: Long,
  meters: Long,
  bounds: Bounds,
)
