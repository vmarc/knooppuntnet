package kpn.server.analyzer.engine.analysis.route.segment

import kpn.server.analyzer.engine.analysis.route.RouteNode

object Path {
  def toNodeIds(paths: Seq[Path]): Set[Seq[Long]] = paths.flatMap(_.segments).map(t => t.nodes.map(_.id)).toSet
}

case class Path(
  start: Option[RouteNode] = None,
  end: Option[RouteNode] = None,
  startNodeId: Long,
  endNodeId: Long,
  segments: Seq[Segment] = Seq.empty,
  broken: Boolean = false
) {

  def meters: Int = segments.map(_.meters).sum

  def routeNodes: Seq[RouteNode] = Seq(start, end).flatten

}
