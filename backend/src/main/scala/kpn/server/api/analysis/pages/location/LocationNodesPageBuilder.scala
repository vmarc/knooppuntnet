package kpn.server.api.analysis.pages.location

import kpn.api.common.Country
import kpn.api.common.Language
import kpn.api.common.RouteType
import kpn.api.common.location.LocationNodesPage
import kpn.api.common.location.LocationNodesParameters
import kpn.api.custom.LocationKey
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.LocationRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class LocationNodesPageBuilder(
  locationRepository: LocationRepository,
  locationService: LocationService
) {

  def build(language: Language, locationKey: LocationKey, parameters: LocationNodesParameters): Option[LocationNodesPage] = {
    if (locationKey == LocationKey(RouteType.cycling, Country.nl, "example")) {
      Some(LocationNodesPageExample.page)
    }
    else {
      buildPage(language, locationKey, parameters)
    }
  }

  private def buildPage(language: Language, locationKeyParam: LocationKey, parameters: LocationNodesParameters): Option[LocationNodesPage] = {
    val subset = locationService.toSubset(language, locationKeyParam)
    val summary = locationRepository.summary(subset)
    val nodes = locationRepository.nodes(subset, parameters)
    val filter = locationRepository.nodeFilterOptions(subset, parameters)
    Some(
      LocationNodesPage(
        TimeInfoBuilder.timeInfo,
        summary,
        filter.totalNodeCount,
        filter,
        nodes
      )
    )
  }
}
