package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class BaseRouteCountryAnalyzerImpl(locationAnalyzer: LocationAnalyzer, routeRepository: RouteRepository) extends BaseRouteCountryAnalyzer {

  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    val countryOption = locationAnalyzer.relationCountry(context.relation) match {
      case Some(country) => Some(country)
      case None => routeRepository.routeCountry(context.relation.id)
    }
    context.copy(
      _countries = Some(countryOption.toSeq),
    )
  }
}
