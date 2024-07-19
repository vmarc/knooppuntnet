package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.data.Node

case class RouteAnalysisElement(
  id: Long,
  direction: RoutePathDirection,
  fromNetworkNode: Option[RouteAnalysisNode],
  toNetworkNode: Option[RouteAnalysisNode],
  fromNodeId: Long,
  toNodeId: Long,
  fragmentGroups: Seq[RouteAnalysisFragmentGroup]
) {
  def fragments: Seq[RouteAnalysisFragment] = {
    fragmentGroups.flatMap(_.fragments)
  }

  def nodes: Seq[Node] = {
    fragments.headOption match {
      case Some(firstFragment) => firstFragment.nodes ++ fragments.tail.flatMap(_.nodes.tail)
      case None => Seq.empty
    }
  }

  def nodeIds: Seq[Long] = {
    fragments.headOption match {
      case Some(firstFragment) => firstFragment.nodeIds ++ fragments.tail.flatMap(_.nodeIds.tail)
      case None => Seq.empty
    }
  }
}
