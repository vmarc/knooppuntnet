package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import org.springframework.stereotype.Component

@Component
class RouteCountryAnalyzer(countryAnalyzer: CountryAnalyzer) extends RouteAnalyzer {

  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val countryOption = countryAnalyzer.relationCountry(context.relation)
    context.copy(
      country = countryOption,
      abort = countryOption.isEmpty
    )
  }
}
