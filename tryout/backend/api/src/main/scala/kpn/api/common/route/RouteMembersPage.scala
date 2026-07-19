package kpn.api.common.route

case class RouteMembersPage(
  routeInfo: RouteInfo,
  structureRows: Seq[StructureRow]
)
