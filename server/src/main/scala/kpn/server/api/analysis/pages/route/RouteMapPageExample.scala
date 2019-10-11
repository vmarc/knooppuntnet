package kpn.server.api.analysis.pages.route

import kpn.shared.route.RouteMapPage

object RouteMapPageExample {
  val page: RouteMapPage = {
    RouteMapPage(RouteDetailsPageExample.page.route, 123)
  }
}
