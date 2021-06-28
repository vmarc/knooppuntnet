package kpn.server.api.analysis.pages.route

import kpn.api.common.route.RouteMapInfo
import kpn.api.common.route.RouteMapPage

object RouteMapPageExample {
  val page: RouteMapPage = {
    val route = RouteDetailsPageExample.page.route
    RouteMapPage(
      RouteMapInfo(
        route._id,
        route.summary.name,
        route.summary.networkType,
        route.analysis.map
      ),
      123
    )
  }
}
