package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object ProposedAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new ProposedAnalyzer(context).analyze
  }
}

class ProposedAnalyzer(context: RouteAnalysisContext) {
  def analyze: RouteAnalysisContext = {
    if (context.relation.tags.has("state", "proposed")) {
      context.copy(proposed = true)
    }
    else {
      context
    }
  }
}
