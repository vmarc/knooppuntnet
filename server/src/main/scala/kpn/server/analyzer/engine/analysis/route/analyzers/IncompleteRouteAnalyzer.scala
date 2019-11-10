package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteIncomplete
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object IncompleteRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new IncompleteRouteAnalyzer(context).analyze
  }
}

class IncompleteRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    if (hasFixmeIncompleteTag) {
      context.withFact(RouteIncomplete)
    }
    else {
      context
    }
  }

  private def hasFixmeIncompleteTag: Boolean = {
    context.loadedRoute.relation.tags.has("fixme", "incomplete")
  }

}
