package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Fact.RouteWithoutWays

object WithoutWaysRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new WithoutWaysRouteAnalyzer(context).analyze
  }
}

class WithoutWaysRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    if (!routeHasWays) {
      context.withFact(RouteWithoutWays)
    }
    else {
      context
    }
  }

  private def routeHasWays: Boolean = {
    context.loadedRoute.relation.members.exists(_.isWay)
  }

}
