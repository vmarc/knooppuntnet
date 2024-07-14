package kpn.server.analyzer.engine.analysis.route

case class RouteNodeAnalysis(
  startNode: Option[RouteNodeData] = None,
  endNode: Option[RouteNodeData] = None,
  startTentacleNodes: Seq[RouteNodeData] = Seq.empty,
  endTentacleNodes: Seq[RouteNodeData] = Seq.empty,
  redundantNodes: Seq[RouteNodeData] = Seq.empty
) {
  def nodes: Seq[RouteNodeData] = startNode.toSeq ++ endNode.toSeq ++ startTentacleNodes ++ endTentacleNodes

  def nodeIds: Seq[Long] = nodes.map(_.node.id)
}
