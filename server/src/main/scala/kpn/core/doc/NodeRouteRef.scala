package kpn.core.doc

import kpn.api.base.WithStringId
import kpn.api.custom.NetworkType

case class NodeRouteRef(
  _id: String,
  nodeId: Long,
  routeId: Long,
  networkType: NetworkType,
  routeName: String
) extends WithStringId
