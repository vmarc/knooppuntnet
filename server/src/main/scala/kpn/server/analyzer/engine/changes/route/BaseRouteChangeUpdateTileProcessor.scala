package kpn.server.analyzer.engine.changes.route

import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.changes.ChangeSetContext

trait BaseRouteChangeUpdateTileProcessor {
  def process(changeSetContext: ChangeSetContext, context: BaseRouteAnalysisContext): ChangeSetContext
}
