package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteTagInvalid
import kpn.api.custom.Fact.RouteTagMissing
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RouteTagRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteTagRouteAnalyzer(context).analyze
  }
}

class RouteTagRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    context.relation.tags("route") match {
      case None => context.withFact(RouteTagMissing)
      case Some(routeTagValue) =>
        if (!isValid(context.relation.tags)) {
          context.withFact(RouteTagInvalid)
        }
        else {
          context
        }
    }
  }

  private def isValid(tags: Tags): Boolean = {
    tags.has("route", context.scopedNetworkType.networkType.routeTagValues:_*)
  }
}
