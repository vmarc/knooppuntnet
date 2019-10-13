package kpn.server.analyzer.engine.changes.orphan.node

import kpn.server.analyzer.load.data.LoadedNode
import kpn.shared.data.raw.RawNode

case class LoadedNodeDelete(rawNode: RawNode, loadedNode: Option[LoadedNode]) {
  def id: Long = rawNode.id
}
