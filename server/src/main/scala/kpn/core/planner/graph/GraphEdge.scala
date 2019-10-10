package kpn.core.planner.graph

import kpn.shared.common.TrackPathKey

case class GraphEdge(
  sourceNodeId: Long,
  sinkNodeId: Long,
  meters: Int,
  pathKey: TrackPathKey
)
