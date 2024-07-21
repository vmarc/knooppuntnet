package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteFixmetodo
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object FixmeTodoRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new FixmeTodoRouteAnalyzer(context).analyze
  }
}

class FixmeTodoRouteAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
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
