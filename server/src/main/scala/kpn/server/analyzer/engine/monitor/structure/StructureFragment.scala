package kpn.server.analyzer.engine.monitor.structure

import kpn.api.common.data.Way

object StructureFragment {
  def from(way: Way, reversed: Boolean = false): StructureFragment = {
    val nodes = if (reversed) way.nodes.reverse else way.nodes
    val nodeIds = nodes.map(_.id)
    StructureFragment(way, nodeIds)
  }
}

case class StructureFragment(way: Way, nodeIds: Seq[Long]) {

  def startNodeId: Long = {
    nodeIds.head
  }

  def endNodeId: Long = {
    nodeIds.last
  }

  def string: String = {
    s"(${way.id}) $startNodeId>$endNodeId"
  }
}
