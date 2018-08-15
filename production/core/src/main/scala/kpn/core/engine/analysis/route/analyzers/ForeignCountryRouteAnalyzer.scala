package kpn.core.engine.analysis.route.analyzers

import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Fact.IgnoreForeignCountry

object ForeignCountryRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new ForeignCountryRouteAnalyzer(context).analyze
  }
}

class ForeignCountryRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    if (context.orphan && context.loadedRoute.country.isEmpty) {
      context.replaceAllFactsWith(IgnoreForeignCountry)
    }
    else {
      context
    }
  }

}
