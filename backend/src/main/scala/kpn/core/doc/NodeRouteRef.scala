package kpn.core.doc

import kpn.api.common.RouteScope
import kpn.api.common.RouteType

case class NodeRouteRef(
  nodeId: Long,
  routeId: Long,
  routeType: RouteType,
  routeScope: RouteScope,
  routeName: String,
  role: Option[String],
) extends Storable
