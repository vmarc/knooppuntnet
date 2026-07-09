package kpn.server.repository

import kpn.api.common.RouteType
import kpn.core.planner.graph.GraphEdge

case class GraphEdges(routeType: RouteType, edges: Seq[GraphEdge])
