package kpn.api.common.node

import kpn.api.common.RouteType

case class NodeOrphanRouteReference(
  routeType: RouteType,
  routeId: Long,
  routeName: String
)
