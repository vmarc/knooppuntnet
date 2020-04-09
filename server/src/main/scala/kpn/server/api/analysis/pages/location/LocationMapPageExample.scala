package kpn.server.api.analysis.pages.location

import kpn.api.common.Bounds
import kpn.api.common.location.LocationMapPage
import kpn.api.common.location.LocationSummary

object LocationMapPageExample {

  def page: LocationMapPage = {
    LocationMapPage(
      LocationSummary(10, 20, 30, 40),
      Bounds(),
      ""
    )
  }
}
