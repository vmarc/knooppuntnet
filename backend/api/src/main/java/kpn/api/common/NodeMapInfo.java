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

/*
package kpn.api.common

case class NodeMapInfo(
  id: Long,
  name: String,
  routeTypes: Seq[RouteType],
  latitude: String,
  longitude: String
)

*/
