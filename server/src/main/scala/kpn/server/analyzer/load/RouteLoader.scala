package kpn.server.analyzer.load

import kpn.api.custom.Relation
import kpn.api.custom.Timestamp

trait RouteLoader {
  def load(timestamp: Timestamp, routeIds: Seq[Long]): Seq[Relation]
}
