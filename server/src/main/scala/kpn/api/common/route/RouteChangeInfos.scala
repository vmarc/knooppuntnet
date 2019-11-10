package kpn.api.common.route

case class RouteChangeInfos(
  changes: Seq[RouteChangeInfo],
  incompleteWarning: Boolean
)
