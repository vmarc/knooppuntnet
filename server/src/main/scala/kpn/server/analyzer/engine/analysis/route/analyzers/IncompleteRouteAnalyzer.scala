package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteIncomplete
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object IncompleteRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new IncompleteRouteAnalyzer(context).analyze
  }
}

class IncompleteRouteAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    if (hasFixmeIncompleteTag) {
      context.withFact(RouteIncomplete).withOldFact(RouteIncomplete)
    }
    else {
      context
    }
  }

  private def hasFixmeIncompleteTag: Boolean = {
    context.relation.hasTag("fixme", "incomplete")
  }
}
