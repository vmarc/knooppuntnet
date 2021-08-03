package kpn.server.analyzer.load

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp

trait NetworkIdsLoader {

  def load(timestamp: Timestamp): Seq[Long]

  def loadByType(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType): Seq[Long]

}
