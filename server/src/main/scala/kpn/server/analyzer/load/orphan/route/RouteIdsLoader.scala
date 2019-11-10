package kpn.server.analyzer.load.orphan.route

import kpn.api.common.ScopedNetworkType
import kpn.api.custom.Timestamp

trait RouteIdsLoader {
  def load(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType): Set[Long]
}
