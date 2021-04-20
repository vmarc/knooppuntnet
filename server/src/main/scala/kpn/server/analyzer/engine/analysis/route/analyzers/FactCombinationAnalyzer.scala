package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteIncomplete
import kpn.api.custom.Fact.RouteNodeMissingInWays
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.api.custom.Fact.RouteRedundantNodes
import kpn.api.custom.Fact.RouteUnusedSegments
import kpn.api.custom.Fact.RouteWithoutWays
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object FactCombinationAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new FactCombinationAnalyzer(context).analyze
  }
}

class FactCombinationAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {

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
