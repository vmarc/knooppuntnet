package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.route.RouteNodes

case class RouteAnalysisNodes(
  startNode: Option[RouteAnalysisNode] = None,
  endNode: Option[RouteAnalysisNode] = None,
  startTentacleNodes: Seq[RouteAnalysisNode] = Seq.empty,
  endTentacleNodes: Seq[RouteAnalysisNode] = Seq.empty,
  redundantNodes: Seq[RouteAnalysisNode] = Seq.empty
) {
  def nodes: Seq[RouteAnalysisNode] = {
    startNode.toSeq ++ endNode.toSeq ++ startTentacleNodes ++ endTentacleNodes ++ redundantNodes
  }

  def nodeIds: Seq[Long] = {
    nodes.map(_.node.id)
  }

  def toRouteNodes: RouteNodes = {
    RouteNodes(
      startNode.map(_.toRouteNode),
      endNode.map(_.toRouteNode),
      startTentacleNodes.map(_.toRouteNode),
      endTentacleNodes.map(_.toRouteNode),
      redundantNodes.map(_.toRouteNode)
    )
  }
}
