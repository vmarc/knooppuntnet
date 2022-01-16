package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.route.Both
import kpn.server.analyzer.engine.analysis.route.OneWayAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteNode

class PathBuilder(allRouteNodes: Set[RouteNode]) {

  def buildPath(segmentFragments: Seq[SegmentFragment], broken: Boolean = false): Option[Path] = {
    if (segmentFragments.isEmpty) {
      None
    }
    else {
      val startNodeId = segmentFragments.head.startNode.id
      val endNodeId = segmentFragments.last.endNode.id
      val start = allRouteNodes.find(routeNode => routeNode.node.id == startNodeId)
      val end = allRouteNodes.find(routeNode => routeNode.node.id == endNodeId)
      val segments = PavedUnpavedSplitter.split(segmentFragments)
      Some(
        Path(
          start,
          end,
          startNodeId,
          endNodeId,
          segments,
          oneWay(segments),
          broken
        )
      )
    }
  }

  private def oneWay(segments: Seq[Segment]): Boolean = {
    segments.exists(_.fragments.exists(fragment => new OneWayAnalyzer(fragment.fragment.way).direction != Both))
  }
}
