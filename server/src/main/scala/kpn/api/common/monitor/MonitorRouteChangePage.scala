package kpn.api.common.monitor

case class MonitorRouteChangePage(
  id: Long,
  ref: Option[String],
  name: String,
  change: MonitorRouteChange
)
