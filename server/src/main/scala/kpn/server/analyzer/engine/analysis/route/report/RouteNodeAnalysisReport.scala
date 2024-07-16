package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.RouteNodeData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RouteNodeAnalysisReport {

  def report(context: RouteDetailAnalysisContext): String = {
    val nodeAnalysis = context.nodeAnalysis
    if (nodeAnalysis.startNode.nonEmpty || nodeAnalysis.endNode.nonEmpty || nodeAnalysis.redundantNodes.nonEmpty) {
      s"""
         |<table>
         |  <tr class="header">
         |    <td colspan="4">RouteNodeAnalysis</td>
         |  </tr>
         |  <tr class="header">
         |    <td>type</td>
         |    <td>node</td>
         |    <td>name</td>
         |    <td>isInWay</td>
         |  </tr>
         |  ${nodeAnalysis.startNode.map(n => routeNodeReport("startNode", n)).mkString}
         |  ${nodeAnalysis.endNode.map(n => routeNodeReport("endNode", n)).mkString}
         |  ${nodeAnalysis.redundantNodes.map(n => routeNodeReport("redundantNode", n)).mkString}
         |</table>
         |""".stripMargin
    }
    else {
      s"""
         |<table>
         |  <tr>
         |    <td>No node network nodes</td>
         |  </tr>
         |</table>
         |""".stripMargin
    }
  }

  private def routeNodeReport(nodeType: String, routeNode: RouteNodeData): String = {
    s"""<tr>
       |  <td>$nodeType</td>
       |  <td>${routeNode.nodeId}</td>
       |  <td>${routeNode.name}</td>
       |  <td>${yes(routeNode.isInWay)}</td>
       |</tr>
       |""".stripMargin
  }

  private def yes(value: Boolean): String = {
    if (value) "yes" else ""
  }
}
