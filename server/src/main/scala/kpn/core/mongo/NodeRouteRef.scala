package kpn.core.mongo

import kpn.api.custom.NetworkType

case class NodeRouteRef(
  _id: String,
  networkType: NetworkType,
  nodeId: Long,
  routeId: Long,
  routeName: String
)
