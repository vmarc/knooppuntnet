package kpn.server.analyzer.engine.analysis.location

import kpn.shared.Location
import kpn.shared.route.RouteInfo

trait RouteNodeBasedLocator {
  def locate(route: RouteInfo): Option[Location]
}
