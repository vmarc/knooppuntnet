package kpn.server.analyzer.load

import kpn.server.analyzer.load.data.LoadedRoute
import kpn.shared.Timestamp

trait RoutesLoader {
  def load(timestamp: Timestamp, routeIds: Seq[Long]): Seq[Option[LoadedRoute]]
}
