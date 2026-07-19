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

/*
package kpn.api.common.subset

import kpn.api.common.Country
import kpn.api.common.RouteType

case class SubsetInfo(
  country: Country,
  routeType: RouteType,
  networkCount: Long = 0,
  factCount: Long = 0,
  changesCount: Long = 0,
  orphanNodeCount: Long = 0,
  orphanRouteCount: Long = 0
)

*/
