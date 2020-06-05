package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.data.Node
import kpn.core.util.Unique
import kpn.server.analyzer.engine.analysis.route.segment.Path
import kpn.server.analyzer.engine.analysis.route.segment.SegmentDirection
import kpn.server.analyzer.engine.analysis.route.segment.SegmentFinder

class TentacleAnalyzer(segmentFinder: SegmentFinder, availableFragmentIds: Set[Int], nodes: Seq[Node]) {

  def findTentacles: Seq[Path] = {
    val tentacles = nodes.combinations(2).toSeq.flatMap { case Seq(start, end) =>
      val pathOption = segmentFinder.find(availableFragmentIds, SegmentDirection.Forward, start, end)
      if (pathOption.isEmpty || pathOption.get.broken) {
        segmentFinder.find(availableFragmentIds, SegmentDirection.Forward, end, start) match {
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
