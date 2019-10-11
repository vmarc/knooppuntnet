package kpn.server.api.analysis.pages

import kpn.server.repository.LocationRepository
import kpn.shared.LocationPage
import kpn.shared.NetworkType
import org.springframework.stereotype.Component

@Component
class LocationPageBuilderImpl(locationRepository: LocationRepository) extends LocationPageBuilder {

  def build(networkType: NetworkType): Option[LocationPage] = {
    Some(
      LocationPage(
        locationRepository.routesWithoutLocation(networkType)
      )
    )
  }

}
