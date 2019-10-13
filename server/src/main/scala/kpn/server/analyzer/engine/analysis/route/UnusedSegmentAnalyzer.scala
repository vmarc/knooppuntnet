package kpn.server.analyzer.engine.analysis.route

import kpn.server.analyzer.engine.analysis.WayAnalyzer
import kpn.server.analyzer.engine.analysis.route.segment.Fragment
import kpn.server.analyzer.engine.analysis.route.segment.Segment
import kpn.server.analyzer.engine.analysis.route.segment.SegmentBuilder

class UnusedSegmentAnalyzer(usedSegments: Iterable[Segment], allFragments: Seq[Fragment]) {

  def find: Seq[Segment] = {

    val usedFragments = usedSegments.flatMap(_.fragments.map(_.fragment)).toSet
    val usedWays = usedFragments.map(_.way)
    val unusedFragments = (allFragments.toSet -- usedFragments).toSeq

    val unused = unusedFragments.filter { fragment =>
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
    new SegmentBuilder().segments(unused)
  }
}
