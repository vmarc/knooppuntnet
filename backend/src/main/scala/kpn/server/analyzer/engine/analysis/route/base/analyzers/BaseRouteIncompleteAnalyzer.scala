package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteIncomplete

object BaseRouteIncompleteAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteIncompleteAnalyzer(context).analyze
  }
}

class BaseRouteIncompleteAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    if (hasFixmeIncompleteTag) {
      context.withFact(RouteIncomplete)
    }
    else {
      context
    }
  }

  private def hasFixmeIncompleteTag: Boolean = {
    context.relation.hasTag("fixme", "incomplete")
  }
}
