package kpn.core.load

import kpn.core.load.data.LoadedRoute
import kpn.shared.Timestamp

trait RouteLoader {
  def loadRoute(timestamp: Timestamp, routeId: Long): Option[LoadedRoute]
}
