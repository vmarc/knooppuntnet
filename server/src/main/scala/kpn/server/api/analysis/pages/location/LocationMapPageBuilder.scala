package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationMapPage
import kpn.api.custom.LocationKey

trait LocationMapPageBuilder {
  def build(locationKey: LocationKey): Option[LocationMapPage]
}
