package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationSummaryPage
import kpn.api.custom.LocationKey

trait LocationSummaryPageBuilder {
  def build(locationKey: LocationKey): Option[LocationSummaryPage]
}
