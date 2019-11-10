package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.data.raw.RawNode
import kpn.server.analyzer.load.data.LoadedNode

case class LoadedNodeDelete(rawNode: RawNode, loadedNode: Option[LoadedNode]) {
  def id: Long = rawNode.id
}
