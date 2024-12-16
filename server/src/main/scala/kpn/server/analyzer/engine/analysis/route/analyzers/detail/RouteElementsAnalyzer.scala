package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerHelper

object RouteElementsAnalyzer extends RouteDetailAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteElementsAnalyzer(context).analyze
  }
}

class RouteElementsAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    val elementIds = RelationAnalyzerHelper.toElementIds(context.relation)
    context.copy(elementIds = elementIds)
  }
}
