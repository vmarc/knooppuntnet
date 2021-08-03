package kpn.server.analyzer.load.orphan.route

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp

trait RouteIdsLoader {

  def load(timestamp: Timestamp): Set[Long]

  def loadByType(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType): Set[Long]

}
