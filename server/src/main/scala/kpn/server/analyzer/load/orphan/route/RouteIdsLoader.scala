package kpn.server.analyzer.load.orphan.route

import kpn.shared.ScopedNetworkType
import kpn.shared.Timestamp

trait RouteIdsLoader {
  def load(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType): Set[Long]
}
