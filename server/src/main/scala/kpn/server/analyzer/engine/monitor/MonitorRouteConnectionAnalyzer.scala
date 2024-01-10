package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.WayMember
import kpn.server.analyzer.engine.analysis.route.RouteWay

case class Connection(nodeId1: Long, nodeId2: Long)

class MonitorRouteConnecctionAnalyzer {

  def analyze(wayMember1: WayMember, wayMember2: WayMember): Seq[RouteWay] = {

    val connectingNodeIds1 = if (wayMember1.role.contains("forward")) {
      Seq(wayMember1.way.nodes.last.id)
    }
    else if (wayMember1.role.contains("backward")) {
      Seq(wayMember1.way.nodes.head.id)
    }
    else {
      Seq(wayMember1.way.nodes.last.id, wayMember1.way.nodes.head.id)
    }

    val connectingNodeIds2 = if (wayMember2.role.contains("forward")) {
      Seq(wayMember2.way.nodes.head.id)
    }
    else if (wayMember2.role.contains("backward")) {
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
          RouteWay(wayMember1.way)
        }
        else {
          RouteWay(wayMember1.way, reversed = true)
        }
        val routeWay2 = if (nodeId == wayMember2.way.nodes.head.id) {
          RouteWay(wayMember2.way)
        }
        else {
          RouteWay(wayMember2.way, reversed = true)
        }
        Seq(routeWay1, routeWay2)
    }
  }
}
