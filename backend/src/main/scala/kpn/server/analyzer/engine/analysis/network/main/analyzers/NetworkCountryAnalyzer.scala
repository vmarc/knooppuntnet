package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class NetworkCountryAnalyzer(locationAnalyzer: LocationAnalyzer) extends NetworkAnalyzer {

  def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    val networkCountry = locationAnalyzer.country(context.nodeDetails) match {
      case None =>
        context.previousKnownCountry
      case Some(country) =>
        Some(country)
    }
    context.copy(
      _country = Some(networkCountry)
    )
  }
}
