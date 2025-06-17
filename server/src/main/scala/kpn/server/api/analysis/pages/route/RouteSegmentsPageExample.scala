package kpn.server.api.analysis.pages.route

import kpn.api.common.RouteType
import kpn.api.common.route.RouteSegmentData
import kpn.api.common.route.RouteSegmentsPage

object RouteSegmentsPageExample {
  val page: RouteSegmentsPage = {
    RouteSegmentsPage(
      RouteSegmentData(
        "route name",
        Seq(RouteType.hiking),
        Seq.empty
      ),
      123,
      3
    )
  }
}
