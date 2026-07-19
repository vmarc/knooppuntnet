package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerHelper

object BaseRouteElementsAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteElementsAnalyzer(context).analyze
  }
}

class BaseRouteElementsAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    val elementIds = RelationAnalyzerHelper.toElementIds(context.relation)
    context.copy(elementIds = elementIds)
  }
}
