package kpn.core.engine.analysis.route.analyzers

import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Country
import kpn.shared.Fact.IgnoreUnsupportedSubset
import kpn.shared.NetworkType

object GermanHikingRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new GermanHikingRouteAnalyzer(context).analyze
  }
}

class GermanHikingRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    if (isGerman && isHiking) {
      context.replaceAllFactsWith(IgnoreUnsupportedSubset)
    }
    else {
      context
    }
  }

  private def isGerman: Boolean = {
    context.loadedRoute.country.contains(Country.de)
  }

  private def isHiking: Boolean = {
    context.loadedRoute.networkType == NetworkType.hiking
  }

}
