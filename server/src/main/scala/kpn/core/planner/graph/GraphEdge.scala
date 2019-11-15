package kpn.core.planner.graph

import kpn.api.common.common.TrackPathKey

case class GraphEdge(
  sourceNodeId: Long,
  sinkNodeId: Long,
  meters: Long,
  pathKey: TrackPathKey
)
