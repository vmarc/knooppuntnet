package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationChangesPage
import kpn.api.custom.LocationKey

trait LocationChangesPageBuilder {
  def build(locationKey: LocationKey): Option[LocationChangesPage]
}
