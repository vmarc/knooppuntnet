package kpn.core.engine.analysis.location

import kpn.shared.Location
import kpn.shared.route.RouteInfo

trait RouteNodeBasedLocator {
  def locate(route: RouteInfo): Option[Location]
}
