package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Fact.RouteIncomplete
import kpn.shared.Fact.RouteIncompleteOk
import kpn.shared.FactLevel

object IncompleteOkRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new IncompleteOkRouteAnalyzer(context).analyze
  }
}

class IncompleteOkRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
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
