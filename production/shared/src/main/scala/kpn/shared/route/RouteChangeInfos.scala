package kpn.shared.route

case class RouteChangeInfos(
  changes: Seq[RouteChangeInfo],
  incompleteWarning: Boolean
)
