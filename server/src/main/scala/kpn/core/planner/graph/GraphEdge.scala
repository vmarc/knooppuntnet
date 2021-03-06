package kpn.core.planner.graph

import kpn.api.common.common.ToStringBuilder
import kpn.api.common.common.TrackPathKey

case class GraphEdge(
  sourceNodeId: Long,
  sinkNodeId: Long,
  meters: Long,
  proposed: Boolean,
  pathKey: TrackPathKey
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("sourceNodeId", sourceNodeId).
    field("sinkNodeId", sinkNodeId).
    field("meters", meters).
    field("proposed", proposed).
    field("pathKey", pathKey).
    build
}
