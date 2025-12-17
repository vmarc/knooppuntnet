package kpn.api.common

case class RouteSummary(
  countries: Seq[Country],
  nodeNetwork: Boolean,
  routeTypes: Seq[RouteType],
  scopes: Seq[RouteScope],
  // TODO redesign - reintroduce routeScope: RouteScope, ?
  name: String,
  meters: Long,
  wayCount: Long
)
