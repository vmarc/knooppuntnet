package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.route.RouteInfo

trait RouteLocator {
  def locate(route: RouteInfo): Option[RouteLocationAnalysis]
}
