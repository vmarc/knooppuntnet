package kpn.api.common;

import kpn.api.common.RouteType;

import com.google.common.collect.ImmutableList;

public record NodeMapInfo(
  Long id,
  String name,
  ImmutableList<RouteType> routeTypes,
  String latitude,
  String longitude
) {
}
