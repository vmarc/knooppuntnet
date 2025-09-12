package kpn.server.api.analysis.pages.location

import kpn.api.common.Country
import kpn.api.common.Language
import kpn.api.common.RouteType
import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRoutesPage
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.custom.LocationKey
import kpn.core.analysis.Facts
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationRoutesPageBuilder(
  locationRepository: LocationRepository,
  locationService: LocationService
) {

  def build(language: Language, locationKey: LocationKey, parameters: LocationRoutesParameters): Option[LocationRoutesPage] = {
    if (locationKey == LocationKey(RouteType.cycling, Country.nl, "example")) {
      Some(LocationRoutesPageExample.page)
    }
    else {
      buildPage(language, locationKey, parameters)
    }
  }

  private def buildPage(language: Language, locationKeyParam: LocationKey, parameters: LocationRoutesParameters): Option[LocationRoutesPage] = {
    val subset = locationService.toSubset(language, locationKeyParam)
    val summary = locationRepository.summary(subset)
    val routes = locationRepository.routes(subset, parameters).map(withoutRedundantFacts)
    val routeCount = locationRepository.routeFilteredCount(subset, parameters)
    val filter = locationRepository.routeFilterOptions(subset, parameters)
    Some(
      LocationRoutesPage(
        TimeInfoBuilder.timeInfo,
        summary,
        routeCount,
        filter,
        routes
      )
    )
  }

  private def withoutRedundantFacts(route: LocationRouteInfo): LocationRouteInfo = {
    if (route.facts.exists(Facts.redundantFacts.contains)) {
      route.copy(facts = Facts.withoutRedundantFacts(route.facts))
    }
    else {
      route
    }
  }
}
