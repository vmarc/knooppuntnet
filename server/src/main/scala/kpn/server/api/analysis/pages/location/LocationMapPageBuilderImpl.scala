package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationMapPage
import kpn.api.common.location.LocationSummary
import kpn.api.custom.LocationKey
import org.springframework.stereotype.Component

@Component
class LocationMapPageBuilderImpl extends LocationMapPageBuilder {
  override def build(locationKey: LocationKey): Option[LocationMapPage] = {
    Some(
      LocationMapPage(LocationSummary(10, 20, 30, 40))
    )
  }
}
