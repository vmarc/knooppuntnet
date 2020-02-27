package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationNodesPage
import kpn.api.common.location.LocationNodesParameters
import kpn.api.common.location.LocationSummary
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationNodesPageBuilderImpl(locationRepository: LocationRepository) extends LocationNodesPageBuilder {

  override def build(locationKey: LocationKey, parameters: LocationNodesParameters): Option[LocationNodesPage] = {
    if (locationKey == LocationKey(NetworkType.cycling, Country.nl, "example")) {
      Some(LocationNodesPageExample.page)
    }
    else {
      buildPage(locationKey, parameters)
    }
  }

  private def buildPage(locationKey: LocationKey, parameters: LocationNodesParameters): Option[LocationNodesPage] = {

    val summary = locationRepository.summary(locationKey)
    val nodes = locationRepository.nodes(locationKey, parameters)

    Some(
      LocationNodesPage(
        TimeInfoBuilder.timeInfo,
        summary,
        nodes
      )
    )
  }
}
