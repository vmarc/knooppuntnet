package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationFactsPage
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationFactsPageBuilderImpl(locationRepository: LocationRepository) extends LocationFactsPageBuilder {

  override def build(locationKey: LocationKey): Option[LocationFactsPage] = {
    if (locationKey == LocationKey(NetworkType.cycling, Country.nl, "example")) {
      Some(LocationFactsPageExample.page)
    }
    else {
      buildPage(locationKey)
    }
  }

  private def buildPage(locationKey: LocationKey): Option[LocationFactsPage] = {
    val summary = locationRepository.summary(locationKey)
    Some(
      LocationFactsPage(summary)
    )
  }
}
