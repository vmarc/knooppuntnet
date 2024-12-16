package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object ProposedAnalyzer extends RouteDetailAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new ProposedAnalyzer(context).analyze
  }
}

class ProposedAnalyzer(context: RouteDetailAnalysisContext) {
  def analyze: RouteDetailAnalysisContext = {
    // TODO redesign - should also look at the 'route' tag for values like "proposed:bicycle"
    if (context.relation.hasTag("state", "proposed")) {
      context.copy(proposed = true)
    }
    else {
      context
    }
  }
}
