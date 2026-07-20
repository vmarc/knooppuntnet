package kpn.api.common.route;

import java.util.Optional;

public record RouteNetworkNodeInfo(
  Long id,
  String name,
  String alternateName,
  Optional<String> longName,
  String lat,
  String lon
) {}
