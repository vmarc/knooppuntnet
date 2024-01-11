package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.WayMember

class MonitorRouteConnectionAnalyzer {

  def analyze(wayMember1: WayMember, wayMember2: WayMember): Option[MonitorRouteFragment] = {

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

    connectingNodeId.map { nodeId =>
      if (nodeId == wayMember1.way.nodes.last.id) {
        MonitorRouteFragment(wayMember1.way)
      }
      else {
        MonitorRouteFragment(wayMember1.way, reversed = true)
      }
    }
  }
}
