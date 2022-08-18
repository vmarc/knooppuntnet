package kpn.api.common.monitor

case class MonitorRouteAdd(
  groupId: String,
  name: String,
  description: String,
  relationId: Option[Long]
)
