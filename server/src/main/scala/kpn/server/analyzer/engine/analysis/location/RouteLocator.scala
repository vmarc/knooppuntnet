package kpn.server.analyzer.engine.analysis.location

import kpn.shared.RouteLocationAnalysis
import kpn.shared.route.RouteInfo

trait RouteLocator {
  def locate(route: RouteInfo): Option[RouteLocationAnalysis]
}
