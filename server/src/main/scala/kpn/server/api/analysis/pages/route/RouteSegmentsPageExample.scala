package kpn.server.api.analysis.pages.route

import kpn.api.common.RouteType
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteSegmentsPage

object RouteSegmentsPageExample {
  val page: RouteSegmentsPage = {
    RouteSegmentsPage(
      RouteInfo(
        routeId = 1,
        routeName = "route name",
        routeTypes = Seq(RouteType.hiking),
        changeCount = 3,
        segmentCount = 2,
      ),
      Seq.empty
    )
  }
}
