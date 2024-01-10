package kpn.server.analyzer.engine.monitor

import kpn.server.analyzer.engine.analysis.route.RouteWay

case class MonitorRouteElement(routeWays: Seq[RouteWay], oneWay: Boolean = false)
