package kpn.server.repository

import kpn.api.custom.NetworkType
import kpn.core.planner.graph.GraphEdge

case class GraphEdges(networkType: NetworkType, edges: Seq[GraphEdge])
