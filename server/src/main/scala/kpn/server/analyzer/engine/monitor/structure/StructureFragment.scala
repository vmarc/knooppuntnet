package kpn.server.analyzer.engine.monitor.structure

import kpn.api.common.data.Node
import kpn.api.common.data.Way

case class StructureFragment(way: Way, /*direction: Option[Direction.Value] = None,*/ reversed: Boolean = false) {

  def startNode: Node = {
    if (reversed) {
      way.nodes.last
    }
    else {
      way.nodes.head
    }
  }

  def endNode: Node = {
    if (reversed) {
      way.nodes.head
    }
    else {
      way.nodes.last
    }
  }

  def nodes: Seq[Node] = {
    if (reversed) {
      way.nodes.reverse
    }
    else {
      way.nodes
    }
  }

  def nodeIds: Seq[Long] = {
    nodes.map(_.id)
  }

  def string: String = {
    s"(${way.id}) ${startNode.id}>${endNode.id}${if (reversed) "*" else ""}"
  }
}
