package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationRoutesPage
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.custom.LocationKey

trait LocationRoutesPageBuilder {
  def build(locationKey: LocationKey, parameters: LocationRoutesParameters): Option[LocationRoutesPage]
}
