package kpn.server.analyzer.load.orphan.route

import kpn.shared.NetworkType
import kpn.shared.Timestamp

trait RouteIdsLoader {
  def load(timestamp: Timestamp, networkType: NetworkType): Set[Long]
}
