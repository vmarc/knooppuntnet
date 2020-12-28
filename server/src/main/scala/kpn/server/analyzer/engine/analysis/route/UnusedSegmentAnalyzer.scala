package kpn.server.analyzer.engine.analysis.route

import kpn.server.analyzer.engine.analysis.route.segment.FragmentMap
import kpn.server.analyzer.engine.analysis.route.segment.Segment
import kpn.server.analyzer.engine.analysis.route.segment.SegmentBuilder

class UnusedSegmentAnalyzer(usedSegments: Iterable[Segment], fragmentMap: FragmentMap) {

  def find: Seq[Segment] = {

    val usedFragments = usedSegments.toVector.flatMap(_.fragments.map(_.fragment))
    val usedWayIds = usedFragments.map(_.way.id)
    val usedFragmentIds = usedFragments.map(_.id)
    val unusedFragmentIds = fragmentMap.ids.filterNot(usedFragmentIds.contains)

    val unused = unusedFragmentIds.filter { fragmentId =>
      val fragment = fragmentMap(fragmentId)
      if (WayAnalyzer.isClosedLoop(fragment.way) || WayAnalyzer.isRoundabout(fragment.way)) {
        if (usedWayIds.contains(fragment.way.id)) {
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
