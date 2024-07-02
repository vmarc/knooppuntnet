package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteCountryAnalyzerImpl(locationAnalyzer: LocationAnalyzer, routeRepository: RouteRepository) extends RouteCountryAnalyzer {

  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    val countryOption = locationAnalyzer.relationCountry(context.relation) match {
      case Some(country) => Some(country)
      case None => routeRepository.routeCountry(context.relation.id)
    }
    context.copy(
      country = countryOption,
      abort = countryOption.isEmpty
    )
  }
}
