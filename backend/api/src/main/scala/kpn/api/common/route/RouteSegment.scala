package kpn.api.common.route

import kpn.api.common.Bounds

case class RouteSegment(
  id: Long,
  startNodeId: Long,
  endNodeId: Long,
  meters: Long,
  bounds: Bounds,
  elementIds: Seq[Long]
)
