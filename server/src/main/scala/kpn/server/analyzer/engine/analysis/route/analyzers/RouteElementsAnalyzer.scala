package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerHelper

object RouteElementsAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteElementsAnalyzer(context).analyze
  }
}

class RouteElementsAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val elementIds = RelationAnalyzerHelper.toElementIds(context.relation)
    context.copy(elementIds = elementIds)
  }

}
