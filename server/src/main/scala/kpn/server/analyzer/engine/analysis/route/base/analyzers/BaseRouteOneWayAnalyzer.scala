package kpn.server.analyzer.engine.analysis.route.base.analyzers

object BaseRouteOneWayAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteOneWayAnalyzer(context).analyze
  }
}

class BaseRouteOneWayAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {

    val oneWayRouteForward = context.relation.hasTag("direction", "forward")
    val oneWayRouteBackward = context.relation.hasTag("direction", "backward")

    val oneWayRoute = context.relation.hasTag("oneway", "yes") || context.relation.hasTag("signed_direction", "yes")

    context.copy(
      _oneWayRouteForward = Some(oneWayRouteForward || oneWayRoute),
      _oneWayRouteBackward = Some(oneWayRouteBackward),
    )
  }
}
