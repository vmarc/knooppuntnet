package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationChangesPage
import kpn.api.common.location.LocationSummary

object LocationChangesPageExample {

  def page: LocationChangesPage = {
    LocationChangesPage(
      LocationSummary(10, 20, 30, 40)
    )
  }
}
