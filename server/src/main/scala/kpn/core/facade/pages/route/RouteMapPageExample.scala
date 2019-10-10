package kpn.core.facade.pages.route

import kpn.shared.route.RouteMapPage

object RouteMapPageExample {
  val page: RouteMapPage = {
    RouteMapPage(RouteDetailsPageExample.page.route, 123)
  }
}
