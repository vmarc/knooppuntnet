package kpn.server.api.analysis.pages.location

import kpn.api.common.Language
import kpn.api.common.location.LocationRoutesPage
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationRoutesPageBuilderImpl(
  locationRepository: LocationRepository,
  locationService: LocationService
) extends LocationRoutesPageBuilder {

  override def build(language: Language, locationKey: LocationKey, parameters: LocationRoutesParameters): Option[LocationRoutesPage] = {
    if (locationKey == LocationKey(NetworkType.cycling, Country.nl, "example")) {
      Some(LocationRoutesPageExample.page)
    }
    else {
      buildPage(language, locationKey, parameters)
    }
  }

  private def buildPage(language: Language, locationKeyParam: LocationKey, parameters: LocationRoutesParameters): Option[LocationRoutesPage] = {
    val locationFilter = locationService.toFilter(language, locationKeyParam)
    val summary = locationRepository.summary(locationFilter)
    val routes = locationRepository.routes(locationFilter, parameters)
    val routeCount = locationRepository.routeFilteredCount(locationFilter, parameters)
    val filter = locationRepository.filterOptions(locationFilter, parameters)
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
}
