package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationRoutesPage
import kpn.api.common.location.LocationSummary
import kpn.api.custom.LocationKey
import kpn.server.api.analysis.pages.TimeInfoBuilder
import org.springframework.stereotype.Component

@Component
class LocationRoutesPageBuilderImpl extends LocationRoutesPageBuilder {
  override def build(locationKey: LocationKey): Option[LocationRoutesPage] = {
    Some(
      LocationRoutesPage(
        TimeInfoBuilder.timeInfo,
        LocationSummary(10, 20, 30, 40),
        Seq.empty
      )
    )
  }
}
