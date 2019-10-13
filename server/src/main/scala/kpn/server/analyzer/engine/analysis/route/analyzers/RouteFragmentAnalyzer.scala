package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.segment.Fragment
import kpn.server.analyzer.engine.analysis.route.segment.FragmentAnalyzer

object RouteFragmentAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteFragmentAnalyzer(context).analyze
  }
}

class RouteFragmentAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    // TODO ROUTE include FragmentAnalyzer into this class
    val fragments: Seq[Fragment] = new FragmentAnalyzer(context.routeNodeAnalysis.get.usedNodes, context.loadedRoute.relation.wayMembers).fragments
    context.copy(fragments = Some(fragments))
  }

}
