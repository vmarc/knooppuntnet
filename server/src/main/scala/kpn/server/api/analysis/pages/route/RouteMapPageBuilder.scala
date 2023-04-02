package kpn.server.api.analysis.pages.route

import kpn.api.common.route.RouteMapPage

trait RouteMapPageBuilder {

  def build(routeId: Long): Option[RouteMapPage]

}
