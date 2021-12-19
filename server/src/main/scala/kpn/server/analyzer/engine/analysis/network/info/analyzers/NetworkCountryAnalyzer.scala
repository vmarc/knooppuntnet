package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import org.springframework.stereotype.Component

@Component
class NetworkCountryAnalyzer(locationAnalyzer: LocationAnalyzer) extends NetworkInfoAnalyzer {

  def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    val country = locationAnalyzer.country(context.nodeDetails) match {
      case None => context.previousKnownCountry
      case Some(country) => Some(country)
    }
    context.copy(
      country = country
    )
  }
}
