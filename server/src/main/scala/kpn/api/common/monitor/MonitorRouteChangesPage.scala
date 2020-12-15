package kpn.api.common.monitor

case class MonitorRouteChangesPage(
  id: Long,
  ref: Option[String],
  name: String,
  changes: Seq[MonitorRouteChangeSummary]
)
