package kpn.core.doc

import kpn.api.base.WithStringId
import kpn.api.common.NetworkType
import kpn.api.custom.NetworkScope

case class NodeRouteRef(
  _id: String,
  nodeId: Long,
  routeId: Long,
  networkType: NetworkType,
  networkScope: NetworkScope,
  routeName: String
) extends WithStringId
