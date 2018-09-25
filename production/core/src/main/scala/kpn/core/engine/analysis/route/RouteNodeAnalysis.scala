package kpn.core.engine.analysis.route

case class RouteNodeAnalysis(
  reversed: Boolean = false,
  startNodes: Seq[RouteNode] = Seq.empty,
  endNodes: Seq[RouteNode] = Seq.empty,
  redundantNodes: Seq[RouteNode] = Seq.empty
) {

  def routeNodes: Seq[RouteNode] = startNodes ++ endNodes ++ redundantNodes

  def nodesInWays: Seq[RouteNode] = routeNodes.filter(_.definedInWay)

  def nodesInRelation: Seq[RouteNode] = routeNodes.filter(_.definedInRelation)

  def usedNodes: Seq[RouteNode] = routeNodes.filter(_.nodeType != RouteNodeType.Redundant)

  def hasStartAndEndNode: Boolean = startNodes.nonEmpty && endNodes.nonEmpty
}
