package kpn.server.analyzer.load.orphan.node

import kpn.shared.NetworkType
import kpn.shared.Timestamp

trait NodeIdsLoader {
  def load(timestamp: Timestamp, networkType: NetworkType): Set[Long]
}
