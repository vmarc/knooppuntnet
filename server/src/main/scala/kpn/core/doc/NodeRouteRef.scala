package kpn.core.doc

import kpn.api.base.WithStringId
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType

case class NodeRouteRef(
  _id: String,
  nodeId: Long,
  routeId: Long,
  networkType: NetworkType,
  networkScope: NetworkScope,
  routeName: String
) extends WithStringId
