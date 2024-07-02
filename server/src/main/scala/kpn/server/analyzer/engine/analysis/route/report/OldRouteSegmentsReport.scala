package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.RouteSegmentData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object OldRouteSegmentsReport {
  def report(context: RouteDetailAnalysisContext): String = {
    s"""<table>
       |<tr class="header">
       |  <td></td>
       |  <td>id</td>
       |  <td>id</td>
       |  <td>startNodeId</td>
       |  <td>endNodeId</td>
       |  <td>meters</td>
       |</tr>
       |${context.segmentAnalysis.routeSegments.map(segmentRow).mkString("\n")}
       |</table>
       |""".stripMargin
  }

  private def segmentRow(routeSegment: RouteSegmentData): String = {
    s"""<tr>
       |
       |  <td>RouteSegmentData</td>
       |  <td>${routeSegment.id}</td>
       |  <td>${routeSegment.segment.id}</td>
       |  <td>${routeSegment.segment.startNodeId}</td>
       |  <td>${routeSegment.segment.endNodeId}</td>
       |  <td>${routeSegment.segment.meters}</td>
       |</tr>
       |""".stripMargin
  }
}
