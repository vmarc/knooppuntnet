package kpn.core.load

import kpn.shared.NetworkType
import kpn.shared.Timestamp

trait NetworkIdsLoader {
  def load(timestamp: Timestamp, networkType: NetworkType): Seq[Long]
}
