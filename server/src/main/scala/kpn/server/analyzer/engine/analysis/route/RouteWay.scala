package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.data.Node
import kpn.api.common.data.Way

case class RouteWay(way: Way, reversed: Boolean = false) {

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
}
