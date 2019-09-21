package kpn.core.load

import kpn.core.load.data.LoadedRoute
import kpn.shared.Timestamp

trait RouteLoader {

  def loadRoute(timestamp: Timestamp, routeId: Long): Option[LoadedRoute]

  def loadRoutes(timestamp: Timestamp, routeIds: Seq[Long]): Seq[LoadedRoute]
}
