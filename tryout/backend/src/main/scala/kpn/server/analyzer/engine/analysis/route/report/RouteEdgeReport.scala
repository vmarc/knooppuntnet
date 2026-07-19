package kpn.server.analyzer.engine.analysis.route.report

import kpn.api.common.route.RouteEdge
import kpn.server.analyzer.engine.analysis.route.report.ReportUtil.osmNodeLink

object RouteEdgeReport {

  def report(edges: Seq[RouteEdge]): String = {
    if (edges.nonEmpty) {
      edgesTable(edges)
    }
    else {
      noEdges()
    }
  }

  private def edgesTable(edges: Seq[RouteEdge]): String = {
    s"""
       |<table>
       |  <tr class="header">
       |    <td>id</td>
       |    <td>source</td>
       |    <td>sink</td>
       |    <td>meters</td>
       |  </tr>
       |  ${edges.map(edgeString).mkString("\n")}
       |</table>
       |""".stripMargin
  }

  private def edgeString(edge: RouteEdge): String = {
    s"""
       |<tr>
       |  <td>edge-${edge.pathId}</td>
       |  <td>${osmNodeLink(edge.sourceNodeId)}</td>
       |  <td>${osmNodeLink(edge.sinkNodeId)}</td>
       |  <td>${edge.meters}</td>
       |</tr>
       |""".stripMargin
  }

  def noEdges(): String = {
    s"""
       |<table>
       |  <tr>
       |    <td>No node network edges</td>
       |  </tr>
       |</table>
       |""".stripMargin
  }
}
