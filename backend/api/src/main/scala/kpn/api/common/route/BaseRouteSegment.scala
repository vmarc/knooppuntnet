package kpn.api.common.route

import kpn.api.common.Bounds

case class BaseRouteSegment(
  id: Long,
  startNodeId: Long,
  endNodeId: Long,
  meters: Long,
  bounds: Bounds,
  elementIds: Seq[Long]
)
