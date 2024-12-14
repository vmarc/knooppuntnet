package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.Fact.RouteIncomplete
import kpn.api.common.Fact.RouteNodeMissingInWays
import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotContinious
import kpn.api.common.Fact.RouteNotForward
import kpn.api.common.Fact.RouteRedundantNodes
import kpn.api.common.Fact.RouteUnusedSegments
import kpn.api.common.Fact.RouteWithoutWays
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object FactCombinationAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new FactCombinationAnalyzer(context).analyze
  }
}

class FactCombinationAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {

    val excludedFacts = context.facts.filter {
      case RouteUnusedSegments => context.hasFact(RouteWithoutWays, RouteIncomplete, RouteNotForward, RouteNotBackward)
      case RouteNotContinious => context.hasFact(RouteNodeMissingInWays, RouteWithoutWays, RouteIncomplete)
      case RouteNotForward => context.hasFact(RouteWithoutWays)
      case RouteNotBackward => context.hasFact(RouteWithoutWays)
      case RouteNodeMissingInWays => context.hasFact(RouteWithoutWays, RouteIncomplete)
      case RouteRedundantNodes => context.hasFact(RouteWithoutWays, RouteIncomplete)
      case _ => false
    }

    context.withoutFacts(excludedFacts: _*)
  }
}
