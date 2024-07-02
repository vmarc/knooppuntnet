package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Country
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class RouteCountryAnalyzerMock(country: Country = Country.be) extends RouteCountryAnalyzer {

  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    context.copy(
      country = Some(country)
    )
  }
}
