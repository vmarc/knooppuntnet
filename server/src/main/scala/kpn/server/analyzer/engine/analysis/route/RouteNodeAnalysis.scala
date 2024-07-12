package kpn.server.analyzer.engine.analysis.route

case class RouteNodeAnalysis(
  reversed: Boolean = false, // TODO redesign - obsolete?
  freeNodes: Seq[RouteNodeData] = Seq.empty, // TODO redesign - obsolete?
  startNode: Option[RouteNodeData] = None,
  endNode: Option[RouteNodeData] = None,
  startTentacleFromNodes: Seq[RouteNodeData] = Seq.empty,
  endTentacleToNodes: Seq[RouteNodeData] = Seq.empty,
  redundantNodes: Seq[RouteNodeData] = Seq.empty
) {
  def nodes: Seq[RouteNodeData] = startNode.toSeq ++ endNode.toSeq ++ startTentacleFromNodes ++ endTentacleToNodes

  def nodeIds: Seq[Long] = nodes.map(_.node.id)
}
