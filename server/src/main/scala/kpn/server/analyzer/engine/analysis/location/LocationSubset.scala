package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.RouteType

case class LocationSubset(
  name: String,
  routeType: RouteType,
  locationIds: Seq[String]
)
