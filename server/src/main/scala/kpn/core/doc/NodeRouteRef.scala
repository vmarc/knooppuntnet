package kpn.core.doc

import kpn.api.base.WithStringId
import kpn.api.common.NetworkScope
import kpn.api.common.NetworkType

case class NodeRouteRef(
  _id: String,
  nodeId: Long,
  routeId: Long,
  networkType: NetworkType,
  networkScope: NetworkScope,
  routeName: String
) extends WithStringId
