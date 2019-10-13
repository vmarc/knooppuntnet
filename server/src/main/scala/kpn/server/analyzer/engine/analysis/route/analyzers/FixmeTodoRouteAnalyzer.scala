package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Fact.RouteFixmetodo

object FixmeTodoRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new FixmeTodoRouteAnalyzer(context).analyze
  }
}

class FixmeTodoRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    if (hasFixmeTodoTag) {
      context.withFact(RouteFixmetodo)
    }
    else {
      context
    }
  }

  private def hasFixmeTodoTag: Boolean = {
    context.loadedRoute.relation.tags.has("fixmetodo")
  }
}
