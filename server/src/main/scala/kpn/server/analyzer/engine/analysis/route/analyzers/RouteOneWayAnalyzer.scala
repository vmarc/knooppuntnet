package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RouteOneWayAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteOneWayAnalyzer(context).analyze
  }
}

class RouteOneWayAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {

    val oneWayRouteForward = context.relation.hasTag("direction", "forward")
    val oneWayRouteBackward = context.relation.hasTag("direction", "backward")

    val oneWayRoute = context.relation.hasTag("oneway", "yes") || context.relation.hasTag("signed_direction", "yes")

    context.copy(
      _oneWayRouteForward = Some(oneWayRouteForward || oneWayRoute),
      _oneWayRouteBackward = Some(oneWayRouteBackward),
    )
  }
}
