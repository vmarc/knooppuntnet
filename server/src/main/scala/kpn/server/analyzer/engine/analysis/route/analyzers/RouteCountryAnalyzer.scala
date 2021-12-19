package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import org.springframework.stereotype.Component

@Component
class RouteCountryAnalyzer(locationAnalyzer: LocationAnalyzer) extends RouteAnalyzer {

  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val countryOption = locationAnalyzer.relationCountry(context.relation)
    context.copy(
      country = countryOption,
      abort = countryOption.isEmpty
    )
  }
}
