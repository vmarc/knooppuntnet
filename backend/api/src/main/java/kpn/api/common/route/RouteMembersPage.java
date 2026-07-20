package kpn.api.common.route;

import kpn.api.common.route.RouteInfo;
import kpn.api.common.route.StructureRow;

import com.google.common.collect.ImmutableList;

public record RouteMembersPage(
  RouteInfo routeInfo,
  ImmutableList<StructureRow> structureRows
) {}
