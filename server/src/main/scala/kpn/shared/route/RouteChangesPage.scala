package kpn.shared.route

import kpn.shared.changes.filter.ChangesFilter

case class RouteChangesPage(
  route: RouteInfo,
  filter: ChangesFilter,
  changes: Seq[RouteChangeInfo],
  incompleteWarning: Boolean,
  totalCount: Int,
  changeCount: Int
)
