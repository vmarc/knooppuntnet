package kpn.server.analyzer.engine.analysis.route.base.analyzers

object BaseRouteProposedAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteProposedAnalyzer(context).analyze
  }
}

class BaseRouteProposedAnalyzer(context: BaseRouteAnalysisContext) {
  def analyze: BaseRouteAnalysisContext = {
    // TODO redesign - should also look at the 'route' tag for values like "proposed:bicycle"
    if (context.relation.hasTag("state", "proposed")) {
      context.copy(proposed = true)
    }
    else {
      context
    }
  }
}
