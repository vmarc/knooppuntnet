package kpn.server.analyzer.engine.changes.orphan.node

import kpn.server.analyzer.load.data.LoadedNode

case class LoadedNodeChange(
  before: LoadedNode,
  after: LoadedNode
) {

  def id: Long = after.id

}
