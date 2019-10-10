package kpn.core.engine.changes.orphan.node

import kpn.core.load.data.LoadedNode

case class LoadedNodeChange(
  before: LoadedNode,
  after: LoadedNode
) {

  def id: Long = after.id

}
