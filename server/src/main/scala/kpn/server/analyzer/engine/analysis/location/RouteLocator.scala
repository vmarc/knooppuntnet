package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.route.RouteMap

trait RouteLocator {
  def locate(routeMap: RouteMap): RouteLocationAnalysis
}
