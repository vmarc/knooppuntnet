package kpn.server.analyzer.engine.analysis.route

import kpn.server.analyzer.engine.analysis.route.structure.RouteAnalysisNode
import kpn.server.analyzer.engine.analysis.route.structure.RouteAnalysisNodes

class RouteNodeAnalysisFormatter(analysis: RouteAnalysisNodes) {

  def nodeStrings: Seq[String] = {
    List(
      nodeStrings("start", analysis.startNode.toSeq),
      nodeStrings("end", analysis.endNode.toSeq),
      nodeStrings("start-tentacle", analysis.startTentacleNodes),
      nodeStrings("end-tentacle", analysis.endTentacleNodes),
      nodeStrings("redundant", analysis.redundantNodes),
    ).flatten
  }

  private def nodeStrings(title: String, nodeDatas: Seq[RouteAnalysisNode]): Seq[String] = {
    nodeDatas.map(n => s"$title=${nodeString(n)}")
  }

  private def nodeString(nodeData: RouteAnalysisNode): String = {
    "%s(%s)%s".format(
      nodeData.node.id,
      nodeData.alternateName,
      if (nodeData.isInWay) "W" else "R",
    )
  }
}
