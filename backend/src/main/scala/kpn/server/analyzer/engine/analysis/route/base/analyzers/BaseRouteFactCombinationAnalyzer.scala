package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteIncomplete
import kpn.api.common.Fact.RouteNodeMissingInWays
import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotForward
import kpn.api.common.Fact.RouteRedundantNodes
import kpn.api.common.Fact.RouteUnusedSegments
import kpn.api.common.Fact.RouteWithoutWays

object BaseRouteFactCombinationAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteFactCombinationAnalyzer(context).analyze
  }
}

class BaseRouteFactCombinationAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {

    val excludedFacts = context.facts.filter {
      case RouteUnusedSegments => context.hasFact(RouteWithoutWays, RouteIncomplete, RouteNotForward, RouteNotBackward)
      case RouteNotForward => context.hasFact(RouteWithoutWays)
      case RouteNotBackward => context.hasFact(RouteWithoutWays)
      case RouteNodeMissingInWays => context.hasFact(RouteWithoutWays, RouteIncomplete)
      case RouteRedundantNodes => context.hasFact(RouteWithoutWays, RouteIncomplete)
      case _ => false
    }

    context.withoutFacts(excludedFacts *)
  }
}
