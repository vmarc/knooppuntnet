package kpn.server.analyzer.engine.analysis.route

class RouteNodeAnalysisFormatter(analysis: RouteNodeAnalysis) {

  def nodeStrings: Seq[String] = {
    List(
      nodeStrings("start", analysis.startNode.toSeq),
      nodeStrings("end", analysis.endNode.toSeq),
      nodeStrings("start-tentacle", analysis.startTentacleNodes),
      nodeStrings("end-tentacle", analysis.endTentacleNodes),
      nodeStrings("redundant", analysis.redundantNodes),
    ).flatten
  }

  private def nodeStrings(title: String, nodeDatas: Seq[RouteNodeData]): Seq[String] = {
    nodeDatas.map(n => s"$title=${nodeString(n)}")
  }

  private def nodeString(nodeData: RouteNodeData): String = {
    "%s(%s)%s".format(
      nodeData.nodeId,
      nodeData.alternateName,
      if (nodeData.isInWay) "W" else "R",
    )
  }
}
