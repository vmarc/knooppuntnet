package kpn.core.load

import kpn.core.load.data.LoadedRoute
import kpn.shared.Timestamp

trait RoutesLoader {
  def load(timestamp: Timestamp, routeIds: Seq[Long]): Seq[Option[LoadedRoute]]
}
