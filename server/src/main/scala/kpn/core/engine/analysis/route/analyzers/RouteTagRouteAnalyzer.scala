package kpn.core.engine.analysis.route.analyzers

import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Fact.RouteTagInvalid
import kpn.shared.Fact.RouteTagMissing

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
