package kpn.api.common.route

case class RouteDetailsPage(
  route: RouteInfo,
  references: RouteReferences,
  changeCount: Int
)
