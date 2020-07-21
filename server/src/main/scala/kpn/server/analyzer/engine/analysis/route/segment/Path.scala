package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.common.TrackPoint
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
  oneWay: Boolean = true,
  broken: Boolean = false
) {

  def meters: Long = segments.map(_.meters).sum

  def routeNodes: Seq[RouteNode] = Seq(start, end).flatten

  def trackPoints: Seq[TrackPoint] = {
    segments.flatMap(_.nodes).map(node => TrackPoint(node.latitude, node.longitude))
  }
}
