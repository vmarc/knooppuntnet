package kpn.server.analyzer.load

import kpn.server.analyzer.load.data.LoadedRoute
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
