package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationFactsPage
import kpn.api.common.location.LocationSummary

object LocationFactsPageExample {

  def page: LocationFactsPage = {
    LocationFactsPage(
      LocationSummary(10, 20, 30, 40)
    )
  }
}
