package kpn.server.api.analysis.pages.location

import kpn.api.common.Fact
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.changes.filter.ServerFilterGroup
import kpn.api.common.common.Reference
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodeOptions
import kpn.api.common.location.LocationNodesPage
import kpn.api.common.location.LocationSummary
import kpn.api.custom.Timestamp
import kpn.server.api.analysis.pages.TimeInfoBuilder

object LocationNodesPageExample {

  def page: LocationNodesPage = {
    LocationNodesPage(
      TimeInfoBuilder.timeInfo,
      LocationSummary(10, 20, 30, 40),
      5,
      LocationNodeOptions(
        ServerFilterGroup("", Seq.empty),
        ServerFilterGroup("", Seq.empty),
        ServerFilterGroup("", Seq.empty),
        ServerFilterGroup("", Seq.empty),
        ServerFilterGroup("", Seq.empty),
        ServerFilterGroup("", Seq.empty),
        ServerFilterGroup("", Seq.empty),
        0
      ),
      Seq(
        LocationNodeInfo(
          rowIndex = 0,
          id = 1001,
          name = "01",
          longName = "Long name",
          latitude = "1",
          longitude = "2",
          lastUpdated = Timestamp(2018, 11, 8),
          lastSurvey = None,
          facts = Seq(Fact.NodeInvalidSurveyDate),
          expectedRouteCount = "3",
          routeReferences = Seq(
            Reference(RouteType.hiking, RouteScope.regional, 101, "01-02"),
            Reference(RouteType.hiking, RouteScope.regional, 102, "01-03")
          )
        )
      )
    )
  }
}
