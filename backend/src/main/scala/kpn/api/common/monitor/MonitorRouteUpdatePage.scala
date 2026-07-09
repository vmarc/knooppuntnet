package kpn.api.common.monitor

case class MonitorRouteUpdatePage(
  groupName: String,
  groupDescription: String,
  routeName: String,
  routeDescription: String,
  groups: Seq[MonitorRouteGroup],
  properties: MonitorRouteProperties
)
