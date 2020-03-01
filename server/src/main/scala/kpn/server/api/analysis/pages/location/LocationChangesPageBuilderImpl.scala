package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationChangesPage
import kpn.api.common.location.LocationChangesParameters
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationChangesPageBuilderImpl(locationRepository: LocationRepository) extends LocationChangesPageBuilder {

  override def build(locationKey: LocationKey, parameters: LocationChangesParameters): Option[LocationChangesPage] = {
    if (locationKey == LocationKey(NetworkType.cycling, Country.nl, "example")) {
      Some(LocationChangesPageExample.page)
    }
    else {
      buildPage(locationKey, parameters)
    }
  }

  private def buildPage(locationKey: LocationKey, parameters: LocationChangesParameters): Option[LocationChangesPage] = {
    val summary = locationRepository.summary(locationKey)
    Some(
      LocationChangesPage(summary)
    )
  }
}
