package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder

case class RouteLegRoute(
  source: RouteLegNode,
  sink: RouteLegNode,
  meters: Long,
  segments: Seq[RouteLegSegment],
  streets: Seq[String]
) {

  def allNodeIds: Set[Long] = Set(source.nodeId.toLong, sink.nodeId.toLong)

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("source", source).
    field("sink", sink).
    field("meters", meters).
    field("segments", segments).
    field("streets", streets).
    build

}
