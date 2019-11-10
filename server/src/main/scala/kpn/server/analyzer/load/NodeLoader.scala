package kpn.server.analyzer.load

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp
import kpn.server.analyzer.load.data.LoadedNode

trait NodeLoader {

  def loadNodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[LoadedNode]

  def load(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType, nodeIds: Seq[Long]): Seq[LoadedNode]

}
