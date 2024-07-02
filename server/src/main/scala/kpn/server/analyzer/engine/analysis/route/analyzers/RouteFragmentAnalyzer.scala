package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.segment.FragmentAnalyzer

object RouteFragmentAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteFragmentAnalyzer(context).analyze
  }
}

class RouteFragmentAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    val usedNodes = context.oldRouteNodeAnalysis.usedNodes
    val wayMembers = context.relation.wayMembers
    val fragmentMap = new FragmentAnalyzer(usedNodes, wayMembers).fragmentMap
    context.copy(_fragmentMap = Some(fragmentMap))
  }
}
