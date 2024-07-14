package kpn.server.analyzer.engine.analysis.route.structure

import kpn.server.analyzer.engine.analysis.route.RouteNodeData

case class RouteAnalysisElement(
  id: Long,
  direction: RoutePathDirection,
  fromNetworkNode: Option[RouteNodeData],
  toNetworkNode: Option[RouteNodeData],
  fromNodeId: Long,
  toNodeId: Long,
  fragmentGroups: Seq[RouteAnalysisFragmentGroup]
) {
  def fragments: Seq[RouteAnalysisFragment] = {
    fragmentGroups.flatMap(_.fragments)
  }

  def nodeIds: Seq[Long] = {
    fragments.headOption match {
      case Some(firstFragment) => firstFragment.nodeIds ++ fragments.tail.flatMap(_.nodeIds.tail)
      case None => Seq.empty
    }
  }
}
