package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.analysis.route.RouteDetailAnalysis

trait RouteTileChangeAnalyzer {

  def impactedTiles(routeAnalysisBefore: RouteDetailAnalysis, routeAnalysisAfter: RouteDetailAnalysis): Seq[String]
}
