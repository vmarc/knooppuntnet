package kpn.server.analyzer.load

import kpn.server.analyzer.load.data.LoadedNode
import kpn.shared.ScopedNetworkType
import kpn.shared.Timestamp

trait NodeLoader {

  def loadNodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[LoadedNode]

  def load(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType, nodeIds: Seq[Long]): Seq[LoadedNode]

}
