package kpn.server.analyzer.load

import kpn.api.custom.Timestamp
import kpn.server.analyzer.load.data.LoadedRoute

trait OldRouteLoader {
  def loadRoute(timestamp: Timestamp, routeId: Long): Option[LoadedRoute]
}
