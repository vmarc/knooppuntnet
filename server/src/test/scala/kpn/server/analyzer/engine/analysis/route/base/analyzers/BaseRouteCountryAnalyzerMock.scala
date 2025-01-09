package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Country

class BaseRouteCountryAnalyzerMock(country: Country = Country.be) extends BaseRouteCountryAnalyzer {

  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    context.copy(
      _countries = Some(Seq(country))
    )
  }
}
