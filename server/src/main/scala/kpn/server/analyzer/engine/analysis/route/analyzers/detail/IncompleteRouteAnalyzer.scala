package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.Fact.RouteIncomplete
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object IncompleteRouteAnalyzer extends RouteDetailAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new IncompleteRouteAnalyzer(context).analyze
  }
}

class IncompleteRouteAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
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
