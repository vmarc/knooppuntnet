package kpn.server.analyzer.engine.analysis.route

class RouteNodeAnalysisFormatter(analysis: RouteNodeAnalysis) {

  def string: String = {
    List(startNodes, endNodes, redundantNodes, reversed).flatten.mkString(",")
  }

  private def startNodes: Option[String] = formatNodes("Start", analysis.startNodes)

  private def endNodes: Option[String] = formatNodes("End", analysis.endNodes)

  private def redundantNodes: Option[String] = formatNodes("Redundant", analysis.redundantNodes)

  private def reversed: Option[String] = if (analysis.reversed) Some("(reversed)") else None

  private def formatNodes(title: String, nodes: Seq[RouteNode]): Option[String] = {
    if (nodes.nonEmpty) {
      val nodeString = nodes.map(formatRouteNode).mkString(",")
      Some(s"$title=($nodeString)")
    }
    else {
      None
    }
  }

  private def formatRouteNode(routeNode: RouteNode): String = "%s/%s/%s/%s%s".format(
    routeNode.node.id,
    routeNode.name,
    routeNode.alternateName,
    if (routeNode.definedInRelation) "R" else "",
    if (routeNode.definedInWay) "W" else ""
  )
}
