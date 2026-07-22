package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.route.RouteNodes

case class RouteNodesAnalysis(
  startNode: Option[RouteNodeAnalysis] = None,
  endNode: Option[RouteNodeAnalysis] = None,
  startTentacleNodes: Seq[RouteNodeAnalysis] = Seq.empty,
  endTentacleNodes: Seq[RouteNodeAnalysis] = Seq.empty,
  redundantNodes: Seq[RouteNodeAnalysis] = Seq.empty
) {
  def nodes: Seq[RouteNodeAnalysis] = {
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
