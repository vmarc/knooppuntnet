package kpn.server.api.analysis.pages.route

import kpn.api.common.route.MapRouteDetail

trait MapRouteDetailBuilder {
  def build(routeId: Long): Option[MapRouteDetail]
}
