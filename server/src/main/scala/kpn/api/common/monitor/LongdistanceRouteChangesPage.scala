package kpn.api.common.monitor

case class LongdistanceRouteChangesPage(
  id: Long,
  ref: Option[String],
  name: String,
  changes: Seq[LongdistanceRouteChangeSummary]
)
