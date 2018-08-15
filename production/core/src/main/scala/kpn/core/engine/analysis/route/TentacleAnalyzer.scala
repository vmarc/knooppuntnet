package kpn.core.engine.analysis.route

import kpn.core.engine.analysis.route.segment.Fragment
import kpn.core.engine.analysis.route.segment.Segment
import kpn.core.engine.analysis.route.segment.SegmentDirection
import kpn.core.engine.analysis.route.segment.SegmentFinder
import kpn.core.util.Unique
import kpn.shared.data.Node

class TentacleAnalyzer(segmentFinder: SegmentFinder, fragments: Seq[Fragment], nodes: Seq[Node]) {

  def findTentacles: Seq[Segment] = {
    val tentacles = nodes.combinations(2).toSeq.flatMap { case Seq(start, end) =>
      val segmentOption = segmentFinder.find(fragments, SegmentDirection.Forward, start, end)
      if (segmentOption.isEmpty || segmentOption.get.broken) {
        segmentFinder.find(fragments, SegmentDirection.Forward, end, start) match {
          case Some(segment) => Some(segment)
          case _ => segmentOption
        }
      }
      else {
        segmentOption
      }
    }
    Unique.filter(tentacles)
  }
}
