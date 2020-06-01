package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationEditPage
import kpn.api.custom.LocationKey

trait LocationEditPageBuilder {
  def build(locationKey: LocationKey): Option[LocationEditPage]
}
