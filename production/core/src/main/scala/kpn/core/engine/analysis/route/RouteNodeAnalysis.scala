package kpn.core.engine.analysis.route

case class RouteNodeAnalysis(
  reversed: Boolean = false,
  startNodes: Seq[RouteNode] = Seq(),
  endNodes: Seq[RouteNode] = Seq(),
  redundantNodes: Seq[RouteNode] = Seq()
) {

  def routeNodes: Seq[RouteNode] = startNodes ++ endNodes ++ redundantNodes

  def nodesInWays: Seq[RouteNode] = routeNodes.filter(_.definedInWay)

  def nodesInRelation: Seq[RouteNode] = routeNodes.filter(_.definedInRelation)

  def usedNodes: Seq[RouteNode] = routeNodes.filter(_.nodeType != RouteNodeType.Redundant)

  def hasStartAndEndNode: Boolean = startNodes.nonEmpty && endNodes.nonEmpty
}
