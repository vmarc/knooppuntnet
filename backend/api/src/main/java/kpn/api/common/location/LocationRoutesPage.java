package kpn.api.common.location;

import kpn.api.common.TimeInfo;
import kpn.api.common.location.LocationRouteInfo;
import kpn.api.common.location.LocationRouteOptions;
import kpn.api.common.location.LocationSummary;

import com.google.common.collect.ImmutableList;

public record LocationRoutesPage(
  TimeInfo timeInfo,
  LocationSummary summary,
  Long routeCount,
  LocationRouteOptions filter,
  ImmutableList<LocationRouteInfo> routes
) {
}

/*
package kpn.api.common.location

import kpn.api.common.TimeInfo

case class LocationRoutesPage(
  timeInfo: TimeInfo,
  summary: LocationSummary,
  routeCount: Long,
  filter: LocationRouteOptions,
  routes: Seq[LocationRouteInfo]
)

*/
