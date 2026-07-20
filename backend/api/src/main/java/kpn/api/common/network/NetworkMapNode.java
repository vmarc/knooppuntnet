package kpn.api.common.network;

import kpn.api.common.LatLon;

public record NetworkMapNode(
  Long id,
  String name,
  String latitude,
  String longitude,
  Boolean roleConnection
) implements LatLon {}
