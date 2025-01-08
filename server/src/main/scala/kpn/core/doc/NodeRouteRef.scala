package kpn.core.doc

import kpn.api.base.WithStringId
import kpn.api.common.NetworkScope
import kpn.api.common.RouteType

case class NodeRouteRef(
  _id: String,
  nodeId: Long,
  routeId: Long,
  routeType: RouteType,
  networkScope: NetworkScope,
  routeName: String
) extends WithStringId
