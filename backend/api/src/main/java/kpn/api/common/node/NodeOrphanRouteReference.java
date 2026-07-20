package kpn.api.common.node;

import kpn.api.common.RouteType;

public record NodeOrphanRouteReference(
  RouteType routeType,
  Long routeId,
  String routeName
) {
}
