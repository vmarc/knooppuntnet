package kpn.api.common.node;

import kpn.api.common.RouteType;

public record NodeOrphanRouteReference(
  RouteType routeType,
  Long routeId,
  String routeName
) {
}

/*
package kpn.api.common.node

import kpn.api.common.RouteType

case class NodeOrphanRouteReference(
  routeType: RouteType,
  routeId: Long,
  routeName: String
)

*/
