package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.WayMember

class MonitorRouteConnectionAnalyzer {

  def analyze(wayMember1: WayMember, wayMember2: WayMember): Seq[MonitorRouteFragment] = {

    val connectingNodeIds1 = if (wayMember1.hasRoleForward) {
      Seq(wayMember1.way.nodes.last.id)
    }
    else if (wayMember1.hasRoleBackward) {
      Seq(wayMember1.way.nodes.head.id)
    }
    else {
      Seq(wayMember1.way.nodes.last.id, wayMember1.way.nodes.head.id)
    }

    val connectingNodeIds2 = if (wayMember2.hasRoleForward) {
      Seq(wayMember2.way.nodes.head.id)
    }
    else if (wayMember2.hasRoleBackward) {
      Seq(wayMember2.way.nodes.last.id)
    }
    else {
      Seq(
        wayMember2.way.nodes.head.id,
        wayMember2.way.nodes.last.id
      )
    }

    val connectingNodeId = connectingNodeIds1.flatMap { nodeId1 =>
      connectingNodeIds2
        .filter(nodeId2 => nodeId1 == nodeId2)
        .headOption
    }.headOption

    connectingNodeId match {
      case None => Seq.empty
      case Some(nodeId) =>
        val routeWay1 = if (nodeId == wayMember1.way.nodes.last.id) {
          MonitorRouteFragment.from(wayMember1)
        }
        else {
          MonitorRouteFragment.fromReversed(wayMember1)
        }
        val routeWay2 = if (nodeId == wayMember2.way.nodes.head.id) {
          MonitorRouteFragment.from(wayMember2)
        }
        else {
          MonitorRouteFragment.fromReversed(wayMember2)
        }
        Seq(routeWay1, routeWay2)
    }
  }
}
