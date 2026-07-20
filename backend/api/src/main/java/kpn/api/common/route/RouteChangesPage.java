package kpn.api.common.route;

import kpn.api.common.changes.filter.ChangesFilterOption;
import kpn.api.common.route.RouteChangeInfo;
import kpn.api.common.route.RouteInfo;

import com.google.common.collect.ImmutableList;

public record RouteChangesPage(
  RouteInfo routeInfo,
  ImmutableList<ChangesFilterOption> filterOptions,
  ImmutableList<RouteChangeInfo> changes
) {
}
