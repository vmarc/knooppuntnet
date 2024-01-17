package kpn.server.analyzer.engine.monitor.structure

import kpn.api.common.data.WayMember

class MonitorRouteNextRouteAnalyzer {

  def analyze(nodeId: Long, wayMember: WayMember): Option[MonitorRouteFragment] = {

    val nodes = wayMember.way.nodes

    val connectingNodeIds = if (wayMember.hasRoleForward) {
      Seq(nodes.head.id)
    }
    else if (wayMember.hasRoleBackward) {
      Seq(nodes.last.id)
    }
    else {
      Seq(
        nodes.head.id,
        nodes.last.id
      )
    }

    if (connectingNodeIds.contains(nodeId)) {
      if (nodeId == nodes.head.id) {
        Some(MonitorRouteFragment(wayMember.way))
      }
      else if (nodeId == nodes.last.id) {
        Some(MonitorRouteFragment(wayMember.way, reversed = true))
      }
      else {
        None
      }
    }
    else {
      None
    }
  }
}
