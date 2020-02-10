package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRoutesPage
import kpn.api.common.location.LocationSummary
import kpn.api.custom.Timestamp
import kpn.server.api.analysis.pages.TimeInfoBuilder

object LocationRoutesPageExample {

  def page: LocationRoutesPage = {
    LocationRoutesPage(
      TimeInfoBuilder.timeInfo,
      LocationSummary(10, 20, 30, 40),
      Seq(
        LocationRouteInfo(
          id = 101,
          name = "01-02",
          length = 100,
          investigate = true,
          accessible = false,
          relationLastUpdated = Timestamp(2018, 8, 11)
        ),
        LocationRouteInfo(
          id = 102,
          name = "01-03",
          length = 130,
          investigate = false,
          accessible = true,
          relationLastUpdated = Timestamp(2018, 8, 13)
        )
      )
    )
  }
}
