package kpn.api.common.route;

import kpn.api.common.route.RouteInfo;
import kpn.api.common.route.RoutePath;

import com.google.common.collect.ImmutableList;

public record RoutePathsPage(
  RouteInfo routeInfo,
  ImmutableList<RoutePath> paths
) {
}
