package kpn.api.common.monitor

case class MonitorRouteInfoPage(
  routeId: Long,
  name: Option[String],
  operator: Option[String],
  ref: Option[String],
  nodeCount: Long,
  wayCount: Long,
  relationCount: Long,
)
