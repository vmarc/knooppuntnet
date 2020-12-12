package kpn.api.common.longdistance

case class LongDistanceRouteChangePage(
  id: Long,
  ref: Option[String],
  name: String,
  change: LongDistanceRouteChange
)
