package kpn.server.analyzer.load

import kpn.api.custom.Timestamp
import kpn.server.analyzer.load.data.LoadedRoute

trait RoutesLoader {
  def load(timestamp: Timestamp, routeIds: Seq[Long]): Seq[Option[LoadedRoute]]
}
