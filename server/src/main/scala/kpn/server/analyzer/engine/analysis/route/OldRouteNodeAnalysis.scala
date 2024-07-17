package kpn.server.analyzer.engine.analysis.route

case class OldRouteNodeAnalysis(
  reversed: Boolean = false,
  freeNodes: Seq[OldRouteNode] = Seq.empty,
  startNodes: Seq[OldRouteNode] = Seq.empty,
  endNodes: Seq[OldRouteNode] = Seq.empty,
  redundantNodes: Seq[OldRouteNode] = Seq.empty
) {

  def routeNodes: Seq[OldRouteNode] = usedNodes ++ redundantNodes

  def nodesInWays: Seq[OldRouteNode] = routeNodes.filter(_.definedInWay)

  def nodesInRelation: Seq[OldRouteNode] = routeNodes.filter(_.definedInRelation)

  def usedNodes: Seq[OldRouteNode] = freeNodes ++ startNodes ++ endNodes

  def hasStartAndEndNode: Boolean = startNodes.nonEmpty && endNodes.nonEmpty
}
