package kpn.server.api.analysis.pages

import kpn.api.common.location.LocationPage
import kpn.api.custom.NetworkType
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationPageBuilderImpl(locationRepository: LocationRepository) extends LocationPageBuilder {

  def build(networkType: NetworkType): Option[LocationPage] = {
    Some(
      LocationPage(Seq())
    )
  }

}
