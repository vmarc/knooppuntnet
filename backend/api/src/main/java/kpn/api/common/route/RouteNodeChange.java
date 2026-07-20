package kpn.api.common.route;

import kpn.api.common.ElementChangeType;

public record RouteNodeChange(
  Long id,
  String latitude,
  String longitude,
  ElementChangeType changeType
) {}
