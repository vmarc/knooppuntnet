package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisNode
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.report.ReportUtil.osmNodeLink

object RouteNodeAnalysisReport {

  def report(context: RouteDetailAnalysisContext): String = {
    val nodes = context.nodes
    if (nodes.startNode.nonEmpty || nodes.endNode.nonEmpty || nodes.redundantNodes.nonEmpty) {
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
         |  ${nodes.startNode.map(n => routeNodeReport("start", n)).mkString}
         |  ${nodes.endNode.map(n => routeNodeReport("end", n)).mkString}
         |  ${nodes.startTentacleNodes.map(n => routeNodeReport("start-tentacle", n)).mkString}
         |  ${nodes.endTentacleNodes.map(n => routeNodeReport("end-tentacle", n)).mkString}
         |  ${nodes.redundantNodes.map(n => routeNodeReport("redundant", n)).mkString}
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

  private def routeNodeReport(nodeType: String, routeNode: RouteAnalysisNode): String = {
    s"""<tr>
       |  <td>$nodeType</td>
       |  <td>${osmNodeLink(routeNode.node.id)}</td>
       |  <td>${routeNode.name}</td>
       |  <td>${yes(routeNode.isInWay)}</td>
       |</tr>
       |""".stripMargin
  }

  private def yes(value: Boolean): String = {
    if (value) "yes" else ""
  }
}
