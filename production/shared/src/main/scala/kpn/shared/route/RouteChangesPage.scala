package kpn.shared.route

case class RouteChangesPage(
  route: RouteInfo,
  changes: Seq[RouteChangeInfo],
  incompleteWarning: Boolean,
  totalCount: Int
)
