package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationChangesPage
import kpn.api.common.location.LocationChangesParameters
import kpn.api.custom.LocationKey

trait LocationChangesPageBuilder {
  def build(locationKey: LocationKey, parameters: LocationChangesParameters): Option[LocationChangesPage]
}
