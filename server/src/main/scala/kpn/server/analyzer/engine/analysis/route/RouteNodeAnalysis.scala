package kpn.server.analyzer.engine.analysis.route

case class RouteNodeAnalysis(
  reversed: Boolean = false, // TODO redesign - obsolete?
  freeNodes: Seq[RouteNodeData] = Seq.empty, // TODO redesign - obsolete?
  startNodes: Seq[RouteNodeData] = Seq.empty,
  endNodes: Seq[RouteNodeData] = Seq.empty,
  redundantNodes: Seq[RouteNodeData] = Seq.empty
) {
  def nodes: Seq[RouteNodeData] = freeNodes ++ startNodes ++ endNodes ++ redundantNodes

  def nodeIds: Seq[Long] = (startNodes ++ endNodes).map(_.node.id)
}
