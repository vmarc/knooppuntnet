package kpn.core.facade.pages

import kpn.core.engine.analysis.location.LocationRepository
import kpn.shared.LocationPage
import kpn.shared.NetworkType

class LocationPageBuilderImpl(locationRepository: LocationRepository) extends LocationPageBuilder {

  def build(networkType: NetworkType): Option[LocationPage] = {
    Some(
      LocationPage(
        locationRepository.routesWithoutLocation(networkType)
      )
    )
  }

}
