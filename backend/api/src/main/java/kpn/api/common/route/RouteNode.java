package kpn.api.common.route;

import kpn.api.common.LatLon;

import java.util.Optional;

public record RouteNode(
  Long nodeId,
  String latitude,
  String longitude,
  String name,
  String alternateName,
  Optional<String> longName,
  Boolean isInWay
) implements LatLon {}
