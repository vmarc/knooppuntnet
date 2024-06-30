package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.analysis.LinkDirection
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

import scala.collection.mutable.ListBuffer

object RoutePathAnalyzer extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val paths = new RoutePathAnalyzer(context).analyze()
    context.copy(
      _paths = Some(paths)
    )
  }
}

class RoutePathAnalyzer(context: RouteAnalysisContext) {
  def analyze(): RoutePaths = {
    val currentSegmentLinks = ListBuffer[RouteLinkWay]()
    val segments = ListBuffer[NewSegment]()
    context.links.routeLinkWays.foreach { link =>
      currentSegmentLinks += link
      if (!link.link.hasNext) {
        val fromNodeId = {
          val link = currentSegmentLinks.head
          if (link.link.direction == LinkDirection.Backward) {
            link.way.nodeIds.last
          }
          else {
            link.way.nodeIds.head
          }
        }
        val toNodeId = {
          val link = currentSegmentLinks.last
          if (link.link.direction == LinkDirection.Backward) {
            link.way.nodeIds.head
          }
          else {
            link.way.nodeIds.last
          }
        }
        segments += NewSegment(segments.size + 1, fromNodeId, toNodeId, currentSegmentLinks.toSeq)
        currentSegmentLinks.clear()
      }
    }
    RoutePaths(segments.toSeq)
  }
}
