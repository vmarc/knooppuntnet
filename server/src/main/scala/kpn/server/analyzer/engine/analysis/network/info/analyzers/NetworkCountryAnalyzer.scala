package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import org.springframework.stereotype.Component

@Component
class NetworkCountryAnalyzer(countryAnalyzer: CountryAnalyzer) extends NetworkInfoAnalyzer {
  def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    val country = countryAnalyzer.country(context.nodeDetails)
    context.copy(
      country = country
    )
  }
}
