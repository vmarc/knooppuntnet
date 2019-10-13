package kpn.server.analyzer.load

import kpn.server.analyzer.load.data.LoadedNode
import kpn.shared.NetworkType
import kpn.shared.Timestamp
import kpn.shared.data.Node

trait NodeLoader {

  def loadNodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[LoadedNode]

  def load(timestamp: Timestamp, networkType: NetworkType, nodeIds: Seq[Long]): Seq[LoadedNode]

}
