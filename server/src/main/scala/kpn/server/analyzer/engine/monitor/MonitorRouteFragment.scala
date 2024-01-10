package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.data.WayMember

object MonitorRouteFragment {

  def from(wayMember: WayMember): MonitorRouteFragment = {
    val oneWay = wayMember.hasRoleForward || wayMember.hasRoleBackward
    MonitorRouteFragment(wayMember.way, reversed = false, oneWay = oneWay)
  }

  def fromReversed(wayMember: WayMember): MonitorRouteFragment = {
    val oneWay = wayMember.hasRoleForward || wayMember.hasRoleBackward
    MonitorRouteFragment(wayMember.way, reversed = true, oneWay = oneWay)
  }
}

case class MonitorRouteFragment(way: Way, reversed: Boolean, oneWay: Boolean) {
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
    s"${startNode.id}>${endNode.id}${if (oneWay) " (oneWay)" else ""}"
  }
}
