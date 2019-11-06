package kpn.server.analyzer.load.orphan.node

import kpn.shared.ScopedNetworkType
import kpn.shared.Timestamp

trait NodeIdsLoader {
  def load(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType): Set[Long]
}
