package kpn.server.analyzer.load.orphan.node

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp

trait NodeIdsLoader {
  def load(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType): Seq[Long]
}
