package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteTagInvalid
import kpn.api.custom.Fact.RouteTagMissing
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RouteTagRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteTagRouteAnalyzer(context).analyze
  }
}

class RouteTagRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    context.loadedRoute.relation.tags("route") match {
      case None => context.withFact(RouteTagMissing)
      case Some(routeTagValue) =>
        if (!isValid(routeTagValue)) {
          context.withFact(RouteTagInvalid)
        }
        else {
          context
        }
    }
  }

  private def isValid(routeTagValue: String): Boolean = {
    context.networkType.routeTagValues.contains(routeTagValue)
  }

}
