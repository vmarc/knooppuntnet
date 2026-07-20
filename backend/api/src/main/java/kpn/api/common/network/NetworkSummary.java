package kpn.api.common.network;

import kpn.api.common.RouteScope;
import kpn.api.common.RouteType;

import java.util.Optional;

public record NetworkSummary(
  Optional<String> name,
  RouteType routeType,
  RouteScope routeScope,
  Long factCount,
  Long nodeCount,
  Long routeCount,
  Long changeCount
) {
}
