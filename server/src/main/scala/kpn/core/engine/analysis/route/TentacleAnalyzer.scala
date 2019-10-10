package kpn.core.engine.analysis.route

import kpn.core.engine.analysis.route.segment.Fragment
import kpn.core.engine.analysis.route.segment.Path
import kpn.core.engine.analysis.route.segment.SegmentDirection
import kpn.core.engine.analysis.route.segment.SegmentFinder
import kpn.core.util.Unique
import kpn.shared.data.Node

class TentacleAnalyzer(segmentFinder: SegmentFinder, fragments: Seq[Fragment], nodes: Seq[Node]) {

  def findTentacles: Seq[Path] = {
    val tentacles = nodes.combinations(2).toSeq.flatMap { case Seq(start, end) =>
      val pathOption = segmentFinder.find(fragments, SegmentDirection.Forward, start, end)
      if (pathOption.isEmpty || pathOption.get.broken) {
        segmentFinder.find(fragments, SegmentDirection.Forward, end, start) match {
          case Some(segment) => Some(segment)
          case _ => pathOption
        }
      }
      else {
        pathOption
      }
    }
    Unique.filter(tentacles)
  }
}
