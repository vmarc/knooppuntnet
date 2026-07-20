package kpn.api.common.location;

import kpn.api.common.changes.filter.ServerFilterGroup;

public record LocationRouteOptions(
  ServerFilterGroup fact,
  ServerFilterGroup survey,
  ServerFilterGroup lastUpdated,
  ServerFilterGroup proposed
) {}
