package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.RouteNodeData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RouteNodeAnalysisReport {

  def report(context: RouteDetailAnalysisContext): String = {
    val routeNodeAnalysis = context.routeNodeAnalysis
    if (routeNodeAnalysis.startNodes.nonEmpty || routeNodeAnalysis.endNodes.nonEmpty || routeNodeAnalysis.freeNodes.nonEmpty || routeNodeAnalysis.redundantNodes.nonEmpty) {
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
         |  ${routeNodeAnalysis.startNodes.map(n => routeNodeReport("startNode", n)).mkString}
         |  ${routeNodeAnalysis.endNodes.map(n => routeNodeReport("endNode", n)).mkString}
         |  ${routeNodeAnalysis.freeNodes.map(n => routeNodeReport("freeNode", n)).mkString}
         |  ${routeNodeAnalysis.redundantNodes.map(n => routeNodeReport("redundantNode", n)).mkString}
         |  <tr><td colspan="4">reversed: ${routeNodeAnalysis.reversed}</td></tr>
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
       |  <td>${routeNode.node.id}</td>
       |  <td>${routeNode.name}</td>
       |  <td>${yes(routeNode.isInWay)}</td>
       |</tr>
       |""".stripMargin
  }

  private def yes(value: Boolean): String = {
    if (value) "yes" else ""
  }
}
