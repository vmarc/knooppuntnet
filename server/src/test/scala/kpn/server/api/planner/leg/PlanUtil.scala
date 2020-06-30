package kpn.server.api.planner.leg

import kpn.api.common.planner.RouteLegFragment
import kpn.api.common.planner.RouteLegNode
import kpn.api.common.planner.RouteLegRoute
import kpn.api.common.planner.RouteLegSegment
import kpn.api.common.route.RouteNetworkNodeInfo

object PlanUtil {

  def toRouteLegRoute(startNode: RouteNetworkNodeInfo, endNode: RouteNetworkNodeInfo): RouteLegRoute = {

    RouteLegRoute(
      source = toRouteLegNode(startNode),
      sink = toRouteLegNode(endNode),
      meters = 0,
      segments = Seq(
        RouteLegSegment(
          meters = 0,
          surface = "unpaved",
          colour = None,
          fragments = Seq(
            RouteLegFragment(
              lat = endNode.lat,
              lon = endNode.lon,
              meters = 0,
              orientation = 1,
              streetIndex = None
            )
          )
        )
      ),
      streets = Seq()
    )
  }

  def toRouteLegNode(node: RouteNetworkNodeInfo): RouteLegNode = {
    RouteLegNode(
      nodeId = node.id.toString,
      nodeName = node.name,
      lat = node.lat,
      lon = node.lon
    )
  }

}
