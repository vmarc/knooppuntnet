package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.Fact.RouteIncomplete
import kpn.api.common.Fact.RouteIncompleteOk
import kpn.core.analysis.Facts
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object IncompleteOkRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new IncompleteOkRouteAnalyzer(context).analyze
  }
}

class IncompleteOkRouteAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
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
