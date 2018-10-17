package kpn.core.engine.analysis.route.analyzers

import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Fact.RouteIncomplete
import kpn.shared.Fact.RouteInvalidSortingOrder
import kpn.shared.Fact.RouteNodeMissingInWays
import kpn.shared.Fact.RouteNotBackward
import kpn.shared.Fact.RouteNotContinious
import kpn.shared.Fact.RouteNotForward
import kpn.shared.Fact.RouteRedundantNodes
import kpn.shared.Fact.RouteReversed
import kpn.shared.Fact.RouteUnusedSegments
import kpn.shared.Fact.RouteWithoutWays

object FactCombinationAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new FactCombinationAnalyzer(context).analyze
  }
}

class FactCombinationAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {

    val excludedFacts = context.facts.filter {
      case RouteUnusedSegments => context.hasFact(RouteWithoutWays, RouteIncomplete, RouteNotForward, RouteNotBackward)
      case RouteInvalidSortingOrder => context.hasFact(RouteWithoutWays, RouteIncomplete, RouteNotForward, RouteNotBackward)
      case RouteReversed => context.hasFact(RouteWithoutWays, RouteIncomplete, RouteNotForward, RouteNotBackward)
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
