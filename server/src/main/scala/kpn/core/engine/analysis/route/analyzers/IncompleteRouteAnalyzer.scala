package kpn.core.engine.analysis.route.analyzers

import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Fact.RouteIncomplete

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
