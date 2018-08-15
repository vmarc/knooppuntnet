package kpn.core.engine.analysis.route.analyzers

import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Fact.IgnoreNetworkTaggedAsRoute

object NetworkTaggedAsRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new NetworkTaggedAsRouteAnalyzer(context).analyze
  }
}

class NetworkTaggedAsRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    if (context.orphan && context.routeNodeAnalysis.get.redundantNodes.size > 5) {
      context.replaceAllFactsWith(IgnoreNetworkTaggedAsRoute)
    }
    else {
      context
    }
  }

}
