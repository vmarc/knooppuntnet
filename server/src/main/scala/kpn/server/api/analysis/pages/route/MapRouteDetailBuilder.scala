package kpn.server.api.analysis.pages.route

import kpn.api.common.route.MapRouteDetail

trait MapRouteDetailBuilder {
  def build(user: Option[String], routeId: Long): Option[MapRouteDetail]
}
