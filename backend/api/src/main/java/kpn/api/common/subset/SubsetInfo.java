package kpn.api.common.subset;

import kpn.api.common.Country;
import kpn.api.common.RouteType;

public record SubsetInfo(
  Country country,
  RouteType routeType,
  Long networkCount,
  Long factCount,
  Long changesCount,
  Long orphanNodeCount,
  Long orphanRouteCount
) {
}
