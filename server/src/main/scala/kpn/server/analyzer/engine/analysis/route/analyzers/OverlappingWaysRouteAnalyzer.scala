package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.Way
import kpn.api.custom.Fact.RouteOverlappingWays
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

case class NodePair(
  nodeId1: Long, // smallest node id
  nodeId2: Long // largest node id
)

case class WayNodePair(
  wayId: Long,
  nodePair: NodePair
)

case class Overlap(
  wayId1: Long,
  wayId2: Long
)

object OverlappingWaysRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new OverlappingWaysRouteAnalyzer(context).analyze
  }
}

class OverlappingWaysRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val overlaps = findOverlaps
    context.copy(overlappingWays = Some(overlaps)).withFact(overlaps.nonEmpty, RouteOverlappingWays)
  }

  private def findOverlaps: Seq[Overlap] = {

    val groupedWayNodePairs = wayNodePairs.groupBy(_.nodePair)
    val overlappingWayNodePairs = groupedWayNodePairs.filter { case (nodePair, wayNodePairList) => wayNodePairList.size > 1 }

    overlappingWayNodePairs.flatMap { case (nodePair, wayNodePairList) =>
      wayNodePairList.combinations(2).map { case Seq(wayNodePair1, wayNodePair2) =>
        Overlap(wayNodePair1.wayId, wayNodePair2.wayId)
      }
    }.toSeq.distinct
  }

  private def wayNodePairs: Seq[WayNodePair] = {
    waysWithMoreThanOneNode.flatMap { way =>
      val wayId = way.id
      val nodeIds = way.nodes.map(_.id)
      val nodePairs = nodeIds.sliding(2).toList.flatMap { case Seq(nodeId1, nodeId2) => toNodePair(nodeId1, nodeId2) }
      nodePairs.map(nodePair => WayNodePair(wayId, nodePair))
    }
  }

  private def waysWithMoreThanOneNode: Seq[Way] = {
    context.loadedRoute.relation.wayMembers.filter(_.way.nodes.size > 1).map(_.way)
  }

  private def toNodePair(nodeId1: Long, nodeId2: Long): Option[NodePair] = {
    if (nodeId1 < nodeId2) {
      Some(NodePair(nodeId1, nodeId2))
    }
    else if (nodeId2 < nodeId1) {
      Some(NodePair(nodeId2, nodeId1))
    }
    else {
      None
    }
  }

}
