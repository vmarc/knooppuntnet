package kpn.server.analyzer.engine.analysis.route

import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodesAnalysis

class RouteNodesAnalysisFormatter(analysis: RouteNodesAnalysis) {

  def nodeStrings: Seq[String] = {
    List(
      nodeStrings("start", analysis.startNode.toSeq),
      nodeStrings("end", analysis.endNode.toSeq),
      nodeStrings("start-tentacle", analysis.startTentacleNodes),
      nodeStrings("end-tentacle", analysis.endTentacleNodes),
      nodeStrings("redundant", analysis.redundantNodes),
    ).flatten
  }

  private def nodeStrings(title: String, nodeDatas: Seq[RouteNodeAnalysis]): Seq[String] = {
    nodeDatas.map(n => s"$title=${nodeString(n)}")
  }

  private def nodeString(nodeData: RouteNodeAnalysis): String = {
    s"${nodeData.node.id}(${nodeData.alternateName})${if (nodeData.isInWay) "W" else "R"}"
  }
}
