package kpn.api.common.route

case class RoutePathsPage(
  routeInfo: RouteInfo,
  paths: Seq[RoutePath],
)
