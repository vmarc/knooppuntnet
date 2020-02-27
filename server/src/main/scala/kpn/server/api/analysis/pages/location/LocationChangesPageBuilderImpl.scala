package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationChangesPage
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationChangesPageBuilderImpl(locationRepository: LocationRepository) extends LocationChangesPageBuilder {

  override def build(locationKey: LocationKey): Option[LocationChangesPage] = {
    if (locationKey == LocationKey(NetworkType.cycling, Country.nl, "example")) {
      Some(LocationChangesPageExample.page)
    }
    else {
      buildPage(locationKey)
    }
  }

  private def buildPage(locationKey: LocationKey): Option[LocationChangesPage] = {
    val summary = locationRepository.summary(locationKey)
    Some(
      LocationChangesPage(summary)
    )
  }
}
