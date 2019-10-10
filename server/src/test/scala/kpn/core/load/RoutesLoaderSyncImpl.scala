package kpn.core.load

import kpn.core.load.data.LoadedRoute
import kpn.shared.Timestamp

class RoutesLoaderSyncImpl(
  routeLoader: RouteLoader
) extends RoutesLoader {

  override def load(timestamp: Timestamp, routeIds: Seq[Long]): Seq[Option[LoadedRoute]] = {
    routeIds.map { routeId =>
      routeLoader.loadRoute(timestamp, routeId)
    }
  }
}
