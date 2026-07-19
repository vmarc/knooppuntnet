package kpn.api.common;

import kpn.api.common.RouteScope;
import kpn.api.common.RouteType;

import java.util.Optional;

public record NodeName(
  RouteType routeType,
  RouteScope routeScope,
  String name,
  Optional<String> longName,
  Boolean proposed
) {
}

/*
package kpn.api.common

import kpn.api.custom.ScopedRouteType

case class NodeName(
  routeType: RouteType,
  routeScope: RouteScope,
  name: String,
  longName: Option[String],
  proposed: Boolean
) {
  def scopedRouteType: ScopedRouteType = {
    ScopedRouteType.from(routeType, routeScope)
  }
}

*/
