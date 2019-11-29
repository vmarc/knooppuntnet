package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.analysis.route.RouteAnalysis

trait TileChangeAnalyzer {

  def analyzeRoute(routeAnalysis: RouteAnalysis): Unit

  def analyzeRouteChange(routeAnalysisBefore: RouteAnalysis, routeAnalysisAfter: RouteAnalysis): Unit

}
