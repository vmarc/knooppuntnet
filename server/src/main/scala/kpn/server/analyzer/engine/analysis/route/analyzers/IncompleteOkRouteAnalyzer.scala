package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteIncomplete
import kpn.api.custom.Fact.RouteIncompleteOk
import kpn.api.custom.FactLevel
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
    context.facts.contains(RouteIncomplete) && !context.facts.exists(_.level == FactLevel.ERROR)
  }
}
