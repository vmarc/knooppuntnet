package kpn.api.common.route

import kpn.api.common.changes.filter.ChangesFilterOption

case class RouteChangesPage(
  routeInfo: RouteInfo,
  filterOptions: Seq[ChangesFilterOption],
  changes: Seq[RouteChangeInfo],
)
