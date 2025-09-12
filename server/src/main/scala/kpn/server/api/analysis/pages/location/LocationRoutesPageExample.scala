package kpn.server.api.analysis.pages.location

import kpn.api.common.changes.filter.ServerFilterGroup
import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRouteOptions
import kpn.api.common.location.LocationRoutesPage
import kpn.api.common.location.LocationSummary
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.server.api.analysis.pages.TimeInfoBuilder

object LocationRoutesPageExample {

  def page: LocationRoutesPage = {
    LocationRoutesPage(
      TimeInfoBuilder.timeInfo,
      LocationSummary(10, 20, 30, 40),
      40,
      LocationRouteOptions(
        ServerFilterGroup("", Seq.empty),
        ServerFilterGroup("", Seq.empty),
        ServerFilterGroup("", Seq.empty),
        ServerFilterGroup("", Seq.empty),
      ),
      Seq(
        LocationRouteInfo(
          0L,
          id = 101,
          name = "01-02",
          meters = 100,
          lastUpdated = Timestamp(2018, 8, 11),
          lastSurvey = Some(Day(2018, 8)),
          symbol = None,
          broken = true,
          proposed = true,
          facts = Seq.empty
        ),
        LocationRouteInfo(
          1L,
          id = 102,
          name = "01-03",
          meters = 130,
          lastUpdated = Timestamp(2018, 8, 13),
          lastSurvey = None,
          symbol = None,
          broken = false,
          proposed = false,
          facts = Seq.empty
        )
      )
    )
  }
}
