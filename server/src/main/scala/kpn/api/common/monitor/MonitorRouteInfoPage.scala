package kpn.api.common.monitor

case class MonitorRouteInfoPage(
  routeId: Long,
  name: Option[String] = None,
  operator: Option[String] = None,
  ref: Option[String] = None,
  nodeCount: Long = 0,
  wayCount: Long = 0,
  relationCount: Long = 0
)
