package kpn.api.common.route

import kpn.api.common.changes.filter.ChangesFilter

case class RouteChangesPage(
  route: RouteInfo,
  filter: ChangesFilter,
  changes: Seq[RouteChangeInfo],
  incompleteWarning: Boolean,
  totalCount: Long,
  changeCount: Long
)
