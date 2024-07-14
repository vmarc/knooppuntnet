package kpn.server.analyzer.engine.analysis.route

class RouteNodeAnalysisFormatter(analysis: RouteNodeAnalysis) {

  def nodeStrings: Seq[String] = {
    List(
      nodeStrings("Start", analysis.startNode.toSeq),
      nodeStrings("End", analysis.endNode.toSeq),
      nodeStrings("Start tentacle from", analysis.startTentacleNodes),
      nodeStrings("End tentacle to", analysis.endTentacleNodes),
      nodeStrings("Redundant", analysis.redundantNodes),
    ).flatten
  }

  private def nodeStrings(title: String, routeNodeDatas: Seq[RouteNodeData]): Seq[String] = {
    routeNodeDatas.map(n => s"$title=(${nodeString(n)})")
  }

  private def nodeString(routeNodeData: RouteNodeData): String = {
    "%s/%s/%s".format(
      routeNodeData.node.id,
      routeNodeData.name,
      if (routeNodeData.isInWay) "W" else "R",
    )
  }
}
