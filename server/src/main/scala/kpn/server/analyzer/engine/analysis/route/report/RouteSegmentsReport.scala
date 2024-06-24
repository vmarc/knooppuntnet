package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.RouteSegmentData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RouteSegmentsReport {
  def report(context: RouteAnalysisContext): String = {
    s"""<pre>
       |routeSegments
       |${context.segmentAnalysis.get.routeSegments.map(segmentReport).mkString("\n")}
       |</pre>
       |""".stripMargin
  }

  private def segmentReport(routeSegment: RouteSegmentData): String = {
    s"  id=${routeSegment.id}, id=${routeSegment.segment.id}, startNodeId=${routeSegment.segment.startNodeId}, endNodeId=${routeSegment.segment.endNodeId}"
  }
}
