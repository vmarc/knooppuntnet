package kpn.server.analyzer.engine.analysis.route

import kpn.server.analyzer.engine.analysis.route.segment.Fragment
import kpn.server.analyzer.engine.analysis.route.segment.Segment
import kpn.core.util.Util
import kpn.shared.data.Way

class RouteSortingOrderAnalyzer(fragments: Seq[Fragment], structure: RouteStructure) {

  private val wayIndexes = fragmentWays(fragments).zipWithIndex.toMap

  def analysis: RouteSortingOrderAnalysis = {
    RouteSortingOrderAnalysis(
      sortingOrderOk(structure.forwardPath.toSeq.flatMap(_.segments)),
      sortingOrderOk(structure.backwardPath.toSeq.flatMap(_.segments)),
      sortingOrderOk(structure.startTentaclePaths.flatMap(_.segments)),
      sortingOrderOk(structure.endTentaclePaths.flatMap(_.segments))
    )
  }

  private def sortingOrderOk(segments: Seq[Segment]): Boolean = {
    segments.forall { segment =>
      val segmentWayIndexes = fragmentWays(segment.fragments.map(_.fragment)).map(way => wayIndexes(way))
      val sorted = segmentWayIndexes.sorted
      segmentWayIndexes == sorted || segmentWayIndexes == sorted.reverse
    }
  }

  private def fragmentWays(frags: Seq[Fragment]): Seq[Way] = {
    Util.withoutSuccessiveDuplicates(frags.map(_.way))
  }
}
