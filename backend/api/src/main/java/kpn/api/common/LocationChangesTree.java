package kpn.api.common;

import kpn.api.common.LocationChangesTreeNode;
import kpn.api.common.RouteType;

import com.google.common.collect.ImmutableList;

public record LocationChangesTree(
  RouteType routeType,
  String locationName,
  Boolean happy,
  Boolean investigate,
  ImmutableList<LocationChangesTreeNode> children
) {
}
