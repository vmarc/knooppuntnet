package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationChangesPage
import kpn.api.common.location.LocationSummary
import kpn.api.custom.LocationKey
import org.springframework.stereotype.Component

@Component
class LocationChangesPageBuilderImpl extends LocationChangesPageBuilder {
  override def build(locationKey: LocationKey): Option[LocationChangesPage] = {
    Some(
      LocationChangesPage(LocationSummary(10, 20, 30, 40))
    )
  }
}
