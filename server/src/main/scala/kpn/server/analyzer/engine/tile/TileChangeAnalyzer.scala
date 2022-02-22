package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.analysis.route.RouteAnalysis

trait TileChangeAnalyzer {

  def impactedTiles(routeAnalysisBefore: RouteAnalysis, routeAnalysisAfter: RouteAnalysis): Seq[String]
}
