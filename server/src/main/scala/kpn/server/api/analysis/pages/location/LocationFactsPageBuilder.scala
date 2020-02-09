package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationFactsPage
import kpn.api.custom.LocationKey

trait LocationFactsPageBuilder {
  def build(locationKey: LocationKey): Option[LocationFactsPage]
}
