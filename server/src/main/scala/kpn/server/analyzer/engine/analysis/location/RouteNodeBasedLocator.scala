package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.location.Location
import kpn.api.common.route.RouteInfo

trait RouteNodeBasedLocator {
  def locate(route: RouteInfo): Option[Location]
}
