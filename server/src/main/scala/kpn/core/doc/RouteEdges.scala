package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.custom.NetworkType
import kpn.core.planner.graph.GraphEdge

case class RouteEdges(
  _id: Long,
  networkType: NetworkType,
  edges: Seq[GraphEdge]
) extends WithId
