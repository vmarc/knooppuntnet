package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.RouteNode
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RouteNodeAnalysisReport {

  def report(context: RouteAnalysisContext): String = {
    val routeNodeAnalysis = context.oldRouteNodeAnalysis
    if (routeNodeAnalysis.routeNodes.nonEmpty) {
      s"""
         |<table>
         |  <tr class="header">
         |    <td colspan="8">RouteNodeAnalysis</td>
         |  </tr>
         |  <tr class="header">
         |    <td colspan="2">type</td>
         |    <td>node</td>
         |    <td>name</td>
         |    <td>alternateName</td>
         |    <td>longName</td>
         |    <td>definedInRelation</td>
         |    <td>definedInWay</td>
         |  </tr>
         |  ${routeNodeAnalysis.freeNodes.map(n => routeNodeReport("freeNode", n)).mkString}
         |  ${routeNodeAnalysis.startNodes.map(n => routeNodeReport("startNode", n)).mkString}
         |  ${routeNodeAnalysis.endNodes.map(n => routeNodeReport("endNode", n)).mkString}
         |  ${routeNodeAnalysis.redundantNodes.map(n => routeNodeReport("redundantNode", n)).mkString}
         |  <tr><td colspan="7">reversed: ${routeNodeAnalysis.reversed}</td></tr>
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

  private def routeNodeReport(nodeType: String, routeNode: RouteNode): String = {
    s"""<tr>
       |  <td>$nodeType</td>
       |  <td>${"" + routeNode.nodeType}</td>
       |  <td>${routeNode.node.id}</td>
       |  <td>${routeNode.name}</td>
       |  <td>${routeNode.alternateName}</td>
       |  <td>${routeNode.longName.getOrElse("")}</td>
       |  <td>${yes(routeNode.definedInRelation)}</td>
       |  <td>${yes(routeNode.definedInWay)}</td>
       |</tr>
       |""".stripMargin
  }

  private def yes(value: Boolean): String = {
    if (value) "yes" else ""
  }
}
