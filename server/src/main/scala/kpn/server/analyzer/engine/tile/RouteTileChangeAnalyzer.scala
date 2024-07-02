package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

trait RouteTileChangeAnalyzer {

  def impactedTiles(before: RouteDetailAnalysisContext, after: RouteDetailAnalysisContext): Seq[String]
}
