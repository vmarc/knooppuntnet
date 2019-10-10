package kpn.core.engine.changes.orphan.node

import kpn.core.load.data.LoadedNode
import kpn.shared.data.raw.RawNode

case class LoadedNodeDelete(rawNode: RawNode, loadedNode: Option[LoadedNode]) {
  def id: Long = rawNode.id
}
