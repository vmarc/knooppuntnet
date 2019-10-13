package kpn.server.analyzer.load

import kpn.server.analyzer.load.data.LoadedRoute
import kpn.shared.Timestamp

trait RouteLoader {
  def loadRoute(timestamp: Timestamp, routeId: Long): Option[LoadedRoute]
}
