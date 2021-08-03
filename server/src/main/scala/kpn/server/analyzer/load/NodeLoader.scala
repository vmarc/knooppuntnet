package kpn.server.analyzer.load

import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Timestamp
import kpn.server.analyzer.load.data.LoadedNode

trait NodeLoader {

  def oldLoadNodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[LoadedNode]

  def oldLoad2(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode]

  def load(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode]

}
