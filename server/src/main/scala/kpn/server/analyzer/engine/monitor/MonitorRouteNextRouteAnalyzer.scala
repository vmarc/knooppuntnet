package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.WayMember

class MonitorRouteNextRouteAnalyzer {

  def analyze(nodeId: Long, wayMember: WayMember): Option[MonitorRouteFragment] = {

    val connectingNodeIds = if (wayMember.hasRoleForward) {
      Seq(wayMember.way.nodes.head.id)
    }
    else if (wayMember.hasRoleBackward) {
      Seq(wayMember.way.nodes.last.id)
    }
    else {
      Seq(
        wayMember.way.nodes.head.id,
        wayMember.way.nodes.last.id
      )
    }

    if (connectingNodeIds.contains(nodeId)) {
      if (nodeId == wayMember.way.nodes.head.id) {
        Some(MonitorRouteFragment.from(wayMember))
      }
      else if (nodeId == wayMember.way.nodes.last.id) {
        Some(MonitorRouteFragment.fromReversed(wayMember))
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
