package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationMapPage
import kpn.api.common.location.LocationSummary
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import org.springframework.stereotype.Component

@Component
class LocationMapPageBuilderImpl extends LocationMapPageBuilder {

  override def build(locationKey: LocationKey): Option[LocationMapPage] = {
    if (locationKey == LocationKey(NetworkType.cycling, Country.nl, "example")) {
      Some(LocationMapPageExample.page)
    }
    else {
      buildPage(locationKey)
    }
  }

  private def buildPage(locationKey: LocationKey): Option[LocationMapPage] = {
    Some(
      LocationMapPage(LocationSummary(10, 20, 30, 40))
    )
  }
}
