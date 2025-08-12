package kpn.database.actions.graph

import kpn.api.common.RouteType
import kpn.core.doc.Storable

// TODO scala3 move back into using class
case class RouteGraphEdge(
  routeType: RouteType,
  proposed: Boolean,
  _id: Long,
  pathId: Long,
  sourceNodeId: Long,
  sinkNodeId: Long,
  meters: Long
) extends Storable

