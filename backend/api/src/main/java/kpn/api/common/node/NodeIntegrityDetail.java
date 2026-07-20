package kpn.api.common.node;

import kpn.api.common.RouteScope;
import kpn.api.common.RouteType;
import kpn.api.common.common.Ref;

import com.google.common.collect.ImmutableList;

public record NodeIntegrityDetail(
  RouteType routeType,
  RouteScope routeScope,
  Integer expectedRouteCount,
  ImmutableList<Ref> routeRefs
) {}

/* TODO migrate

  def failed: Boolean = {
    routeRefs.sizeIs != expectedRouteCount
  }

  def hasScopedRouteType(scopedRouteType: ScopedRouteType): Boolean = {
    scopedRouteType.routeType == routeType && scopedRouteType.routeScope == routeScope
  }

*/
