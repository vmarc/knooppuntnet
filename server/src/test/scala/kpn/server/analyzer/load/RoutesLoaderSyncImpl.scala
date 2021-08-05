package kpn.server.analyzer.load

import kpn.api.custom.Timestamp
import kpn.server.analyzer.load.data.LoadedRoute

class RoutesLoaderSyncImpl(
  routeLoader: OldRouteLoader
) extends RoutesLoader {

  override def load(timestamp: Timestamp, routeIds: Seq[Long]): Seq[Option[LoadedRoute]] = {
    routeIds.map { routeId =>
      routeLoader.loadRoute(timestamp, routeId)
    }
  }
}
