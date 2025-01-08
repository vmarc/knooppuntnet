package kpn.api.common

import kpn.api.custom.ScopedRouteType

case class NodeName(
  routeType: RouteType,
  networkScope: NetworkScope,
  name: String,
  longName: Option[String],
  proposed: Boolean
) {
  def scopedRouteType: ScopedRouteType = {
    ScopedRouteType.from(networkScope, routeType)
  }
}
