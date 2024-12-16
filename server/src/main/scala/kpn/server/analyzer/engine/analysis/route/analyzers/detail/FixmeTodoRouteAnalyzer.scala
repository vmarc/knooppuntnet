package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.Fact.RouteFixmetodo
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object FixmeTodoRouteAnalyzer extends RouteDetailAnalyzer {
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
