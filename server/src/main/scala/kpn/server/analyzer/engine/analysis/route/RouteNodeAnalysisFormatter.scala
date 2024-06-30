package kpn.server.analyzer.engine.analysis.route

class RouteNodeAnalysisFormatter(analysis: RouteNodeAnalysis) {

  def nodeStrings: Seq[String] = {
    List(
      nodeStrings("Free", analysis.freeNodes),
      nodeStrings("Start", analysis.startNodes),
      nodeStrings("End", analysis.endNodes),
      nodeStrings("Redundant", analysis.redundantNodes),
      if (analysis.reversed) Seq("(reversed)") else Seq.empty
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
