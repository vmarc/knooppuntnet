package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object ProposedAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new ProposedAnalyzer(context).analyze
  }
}

class ProposedAnalyzer(context: RouteAnalysisContext) {
  def analyze: RouteAnalysisContext = {
    // TODO redesign - should also look at the 'route' tag for values like "proposed:bicycle"
    if (context.relation.hasTag("state", "proposed")) {
      context.copy(proposed = true)
    }
    else {
      context
    }
  }
}
