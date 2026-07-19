package kpn.api.common.route;

import kpn.api.common.route.RouteDetails;
import kpn.api.common.route.RouteInfo;

public record RouteDetailsPage(
  RouteInfo routeInfo,
  RouteDetails details
) {
}

/*
package kpn.api.common.route

case class RouteDetailsPage(
  routeInfo: RouteInfo,
  details: RouteDetails,
)

*/
