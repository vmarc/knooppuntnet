package kpn.server.analyzer.load.orphan.route

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp

trait RouteIdsLoader {

  def loadByType(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType): Set[Long]

}
