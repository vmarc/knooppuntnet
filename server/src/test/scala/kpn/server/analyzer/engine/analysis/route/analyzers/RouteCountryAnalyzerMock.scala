package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.Country
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class RouteCountryAnalyzerMock(country: Country = Country.be) extends RouteCountryAnalyzer {

  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    context.copy(
      _countries = Some(Seq(country))
    )
  }
}
