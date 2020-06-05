package kpn.server.analyzer.engine.analysis.route

import kpn.server.analyzer.engine.analysis.route.segment.Fragment
import kpn.server.analyzer.engine.analysis.route.segment.Segment
import kpn.server.analyzer.engine.analysis.route.segment.SegmentBuilder

class UnusedSegmentAnalyzer(usedSegments: Iterable[Segment], fragmentMap: Map[Int, Fragment]) {

  def find: Seq[Segment] = {

    val usedFragments = usedSegments.flatMap(_.fragments.map(_.fragment)).toSet
    val usedWays = usedFragments.map(_.way)
    val unusedFragmentIds = fragmentMap.values.map(_.id).toSet -- usedFragments.map(_.id)

    val unused = unusedFragmentIds.filter { fragmentId =>
      val fragment = fragmentMap(fragmentId)
      if (WayAnalyzer.isClosedLoop(fragment.way) || WayAnalyzer.isRoundabout(fragment.way)) {
        if (usedWays.contains(fragment.way)) {
          false
        }
        else {
          true
        }
      }
      else {
        true
      }
    }
    new SegmentBuilder(fragmentMap).segments(unused)
  }
}
