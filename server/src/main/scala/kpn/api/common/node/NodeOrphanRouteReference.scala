package kpn.api.common.node

import kpn.api.custom.NetworkType

case class NodeOrphanRouteReference(
  networkType: NetworkType,
  routeId: Long,
  routeName: String
)
