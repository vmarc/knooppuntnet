package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteIncomplete
import kpn.api.common.Fact.RouteIncompleteOk
import kpn.core.analysis.Facts

object BaseRouteIncompleteOkAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteIncompleteOkAnalyzer(context).analyze
  }
}

class BaseRouteIncompleteOkAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    if (hasFixmeIncompleteTagButLooksOk) {
      context.withFact(RouteIncompleteOk)
    }
    else {
      context
    }
  }

  private def hasFixmeIncompleteTagButLooksOk: Boolean = {
    context.facts.contains(RouteIncomplete) && !context.facts.exists(Facts.isError)
  }
}
