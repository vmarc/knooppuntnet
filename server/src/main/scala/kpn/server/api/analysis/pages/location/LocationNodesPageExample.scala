package kpn.server.api.analysis.pages.location

import kpn.api.common.common.Ref
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesPage
import kpn.api.common.location.LocationSummary
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.server.api.analysis.pages.TimeInfoBuilder

object LocationNodesPageExample {

  def page: LocationNodesPage = {
    LocationNodesPage(
      TimeInfoBuilder.timeInfo,
      LocationSummary(10, 20, 30, 40),
      Seq(
        LocationNodeInfo(
          id = 1001,
          name = "01",
          number = "1",
          latitude = "1",
          longitude = "2",
          definedInRoute = false,
          timestamp = Timestamp(2018, 11, 8),
          routeReferences = Seq(
            Ref(101, "01-02"),
            Ref(102, "01-03")
          ),
          facts = Seq(Fact.RouteNotBackward),
          tags = Tags.empty
        )
      )
    )
  }
}
