package kpn.api.common.route

import kpn.api.common.Bounds
import kpn.api.common.RouteType

case class RoutePathData(
  name: String,
  routeTypes: Seq[RouteType],
  paths: Seq[RoutePath],
  bounds: Option[Bounds],
)
