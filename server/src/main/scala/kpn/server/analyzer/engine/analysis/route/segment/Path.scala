package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.common.TrackPoint
import kpn.server.analyzer.engine.analysis.route.OldRouteNode

object Path {
  def toNodeIds(paths: Seq[Path]): Set[Seq[Long]] = paths.flatMap(_.segments).map(t => t.nodes.map(_.id)).toSet
}

case class Path(
  start: Option[OldRouteNode] = None,
  end: Option[OldRouteNode] = None,
  startNodeId: Long,
  endNodeId: Long,
  segments: Seq[Segment] = Seq.empty,
  oneWay: Boolean = true,
  broken: Boolean = false
) {

  def meters: Long = segments.map(_.meters).sum

  def routeNodes: Seq[OldRouteNode] = Seq(start, end).flatten

  def trackPoints: Seq[TrackPoint] = {
    segments.flatMap(_.nodes).map(node => TrackPoint(node.latitude, node.longitude))
  }
}
