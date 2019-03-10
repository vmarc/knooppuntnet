package kpn.core.engine.analysis.route.segment

import kpn.core.engine.analysis.route.RouteNode

case class Path(
  start: Option[RouteNode] = None,
  end: Option[RouteNode] = None,
  startNodeId: Long,
  endNodeId: Long,
  segments: Seq[Segment] = Seq.empty,
  broken: Boolean = false
) {
  def meters: Int = segments.map(_.meters).sum
}
