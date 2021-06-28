package kpn.api.common.route

import kpn.api.common.changes.filter.ChangesFilter

case class RouteChangesPage(
  routeNameInfo: RouteNameInfo,
  filter: ChangesFilter,
  changes: Seq[RouteChangeInfo],
  incompleteWarning: Boolean,
  totalCount: Long,
  changeCount: Long
)
