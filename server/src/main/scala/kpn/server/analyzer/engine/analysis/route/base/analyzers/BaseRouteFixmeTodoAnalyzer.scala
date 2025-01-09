package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteFixmetodo

object BaseRouteFixmeTodoAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteFixmeTodoAnalyzer(context).analyze
  }
}

class BaseRouteFixmeTodoAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    if (hasFixmeTodoTag) {
      context.withFact(RouteFixmetodo)
    }
    else {
      context
    }
  }

  private def hasFixmeTodoTag: Boolean = {
    context.relation.hasTag("fixmetodo")
  }
}
