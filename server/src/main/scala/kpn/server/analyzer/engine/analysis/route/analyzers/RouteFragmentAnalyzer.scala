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
    val usedNodes = context.routeNodeAnalysis.get.usedNodes
    val wayMembers = context.loadedRoute.relation.wayMembers
    val fragmentMap = new FragmentAnalyzer(usedNodes, wayMembers).fragmentMap
    context.copy(fragmentMap = Some(fragmentMap))
  }

}
