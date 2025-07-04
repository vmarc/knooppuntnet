package kpn.core.doc

import kpn.api.common.Bounds

case class SuperSegmentElementInfo(
  id: Long,
  relationId: Long,
  osmSegmentId: Long,
  startNodeId: Long,
  endNodeId: Long,
  meters: Long,
  bounds: Bounds,
)
