package kpn.api.common.node

import kpn.api.common.NetworkType

case class NodeOrphanRouteReference(
  networkType: NetworkType,
  routeId: Long,
  routeName: String
)
