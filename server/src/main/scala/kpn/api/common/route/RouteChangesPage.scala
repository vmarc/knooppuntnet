package kpn.api.common.route

import kpn.api.common.changes.filter.ChangesFilterOption

case class RouteChangesPage(
  routeNameInfo: RouteNameInfo,
  filterOptions: Seq[ChangesFilterOption],
  changes: Seq[RouteChangeInfo],
  totalCount: Long,
  changeCount: Long
)
