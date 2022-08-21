package kpn.api.common.monitor

case class MonitorRouteAdd(
  name: String,
  description: String,
  relationId: Option[String]
)
