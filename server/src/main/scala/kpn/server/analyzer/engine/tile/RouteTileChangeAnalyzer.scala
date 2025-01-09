package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext

trait RouteTileChangeAnalyzer {

  def impactedTiles(before: BaseRouteAnalysisContext, after: BaseRouteAnalysisContext): Seq[String]
}
