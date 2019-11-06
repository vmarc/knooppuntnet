package kpn.server.analyzer.load

import kpn.shared.ScopedNetworkType
import kpn.shared.Timestamp

trait NetworkIdsLoader {
  def load(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType): Seq[Long]
}
