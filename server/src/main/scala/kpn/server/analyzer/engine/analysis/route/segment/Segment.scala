package kpn.server.analyzer.engine.analysis.route.segment

import kpn.shared.data.Node

object Segment {
  def toNodeIds(segments: Seq[Segment]): Set[Seq[Long]] = segments.map(t => t.nodes.map(_.id)).toSet
}

case class Segment(surface: String, fragments: Seq[SegmentFragment]) {

  def meters: Int = fragments.map(_.fragment.meters).sum

  def nodes: Seq[Node] = {
    if (fragments.isEmpty) {
      Seq()
    }
    else if (fragments.size == 1) {
      fragments.head.nodes
    }
    else {
      fragments.head.nodes ++ fragments.tail.flatMap(_.nodes.tail)
    }
  }

}
