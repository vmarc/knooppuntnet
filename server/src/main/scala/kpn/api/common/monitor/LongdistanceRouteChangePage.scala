package kpn.api.common.monitor

case class LongdistanceRouteChangePage(
  id: Long,
  ref: Option[String],
  name: String,
  change: LongdistanceRouteChange
)
