package kpn.server.analyzer.engine.analysis.route

case class RouteNodeAnalysis(
  reversed: Boolean = false,
  freeNodes: Seq[RouteNode] = Seq.empty,
  startNodes: Seq[RouteNode] = Seq.empty,
  endNodes: Seq[RouteNode] = Seq.empty,
  redundantNodes: Seq[RouteNode] = Seq.empty
) {

  def routeNodes: Seq[RouteNode] = usedNodes ++ redundantNodes

  def nodesInWays: Seq[RouteNode] = routeNodes.filter(_.definedInWay)

  def nodesInRelation: Seq[RouteNode] = routeNodes.filter(_.definedInRelation)

  def usedNodes: Seq[RouteNode] = freeNodes ++ startNodes ++ endNodes

  def hasStartAndEndNode: Boolean = startNodes.nonEmpty && endNodes.nonEmpty
}
