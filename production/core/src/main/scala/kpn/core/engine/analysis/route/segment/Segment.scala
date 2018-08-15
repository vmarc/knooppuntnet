package kpn.core.engine.analysis.route.segment

import kpn.core.engine.analysis.route.RouteNode
import kpn.shared.data.Node

object Segment {
  def toNodeIds(segments: Seq[Segment]): Set[Seq[Long]] = segments.map(t => t.nodes.map(_.id)).toSet
}

case class Segment(
  start: Option[RouteNode] = None,
  end: Option[RouteNode] = None,
  segmentFragments: Seq[SegmentFragment] = Seq(),
  broken: Boolean = false,
  surface: Option[String] = None
) {

  override def toString: String = new SegmentFormatter(this).string

  def meters: Int = segmentFragments.map(_.fragment.meters).sum

  def nodes: Seq[Node] = {
    if (segmentFragments.isEmpty) {
      Seq()
    }
    else if (segmentFragments.size == 1) {
      segmentFragments.head.nodes
    }
    else {
      segmentFragments.head.nodes ++ segmentFragments.tail.flatMap(_.nodes.tail)
    }
  }

  def routeNodes: Seq[RouteNode] = Seq(start, end).flatten

}
