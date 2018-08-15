package kpn.core.engine.analysis.route.segment

import kpn.core.engine.analysis.route.analyzers.NodePair
import kpn.core.engine.analysis.route.analyzers.Overlap
import kpn.core.engine.analysis.route.analyzers.WayNodePair
import kpn.shared.data.WayMember

class _OldOverlapAnalyzer(wayMembers: Seq[WayMember]) {

  def overlappingWays: Seq[Overlap] = {

    val wayNodePairs = wayMembers.filter(_.way.nodes.size > 1).flatMap { wayMember =>
      val wayId = wayMember.way.id
      val nodeIds = wayMember.way.nodes.map(_.id)
      val nodePairs = nodeIds.sliding(2).toList.flatMap { case Seq(nodeId1, nodeId2) => toNodePair(nodeId1, nodeId2) }
      nodePairs.map(nodePair => WayNodePair(wayId, nodePair))
    }

    val groupedWayNodePairs = wayNodePairs.groupBy(_.nodePair)
    val overlappingWayNodePairs = groupedWayNodePairs.filter { case (nodePair, wayNodePairList) => wayNodePairList.size > 1 }

    overlappingWayNodePairs.flatMap { case (nodePair, wayNodePairList) =>
      wayNodePairList.combinations(2).map { case Seq(wayNodePair1, wayNodePair2) =>
        Overlap(wayNodePair1.wayId, wayNodePair2.wayId)
      }
    }.toSeq.distinct
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
