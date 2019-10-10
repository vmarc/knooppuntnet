package kpn.core.engine.analysis.location

import kpn.shared.RouteLocationAnalysis
import kpn.shared.route.RouteInfo

trait RouteWayBasedLocator {
  def locate(route: RouteInfo): Option[RouteLocationAnalysis]
}
