package kpn.api.common.monitor

case class MonitorChangesPage(
  id: Long,
  ref: Option[String],
  name: String,
  changes: Seq[MonitorRouteChangeSummary]
)
