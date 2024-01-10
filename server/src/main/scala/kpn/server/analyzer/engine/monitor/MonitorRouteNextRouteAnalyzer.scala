package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.WayMember
import kpn.server.analyzer.engine.analysis.route.RouteWay

class MonitorRouteNextRouteAnalyzer {

  def analyze(nodeId: Long, wayMember: WayMember): Option[RouteWay] = {

    val connectingNodeIds = if (wayMember.role.contains("forward")) {
      Seq(wayMember.way.nodes.head.id)
    }
    else if (wayMember.role.contains("backward")) {
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
        Some(RouteWay(wayMember.way))
      }
      else if (nodeId == wayMember.way.nodes.last.id) {
        Some(RouteWay(wayMember.way, reversed = true))
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
