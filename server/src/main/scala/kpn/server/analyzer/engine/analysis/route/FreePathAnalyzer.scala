package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.data.Node
import kpn.core.util.Unique
import kpn.server.analyzer.engine.analysis.route.segment.AsIsLoopPathBuilder
import kpn.server.analyzer.engine.analysis.route.segment.Path
import kpn.server.analyzer.engine.analysis.route.segment.SegmentDirection
import kpn.server.analyzer.engine.analysis.route.segment.SegmentFinder

class FreePathAnalyzer(
  segmentFinder: SegmentFinder,
  asIsLoopPathBuilder: AsIsLoopPathBuilder,
  availableFragmentIds: Set[Int],
  nodes: Seq[Node]
) {

  def findTentacles: Seq[Path] = {
    if (nodes.size == 1) {
      asIsLoopPathBuilder.build() match {
        case Some(path) => Seq(path)
        case None => findPaths()
      }
    }
    else {
      findPaths()
    }
  }

  private def findPaths(): Seq[Path] = {
    val tentacles = nodes.combinations(2).toSeq.flatMap { case Seq(start, end) =>
      Seq(
        findPath(start, end).toSeq,
        findPath(end, start).toSeq
      ).flatten
    }
    Unique.filter(tentacles)
  }

  private def findPath(start: Node, end: Node) = {
    val pathOption = segmentFinder.find(availableFragmentIds, SegmentDirection.Forward, start, end)
    if (pathOption.isEmpty || pathOption.get.broken) {
      segmentFinder.find(availableFragmentIds, SegmentDirection.Forward, end, start) match {
        case Some(segment) =>
          Some(segment)
        case _ => pathOption
      }
    }
    else {
      pathOption
    }
  }
}
