package kpn.api.common.route

import kpn.api.common.Bounds

case class SuperSubSegmentInfo(
  id: Long,
  relationId: Long,
  segmentId: Long,
  startNodeId: Long,
  endNodeId: Long,
  meters: Long,
  bounds: Bounds,
)
