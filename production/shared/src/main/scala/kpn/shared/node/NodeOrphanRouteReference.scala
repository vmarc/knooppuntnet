package kpn.shared.node

import kpn.shared.NetworkType

case class NodeOrphanRouteReference(
  networkType: NetworkType,
  routeId: Long,
  routeName: String
)
