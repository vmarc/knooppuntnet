package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.segment.FragmentAnalyzer

object RouteFragmentAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteFragmentAnalyzer(context).analyze
  }
}

class RouteFragmentAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val fragmentMap = new FragmentAnalyzer(context.routeNodeAnalysis.get.usedNodes, context.loadedRoute.relation.wayMembers).fragmentMap
    context.copy(fragmentMap = Some(fragmentMap))
  }

}
