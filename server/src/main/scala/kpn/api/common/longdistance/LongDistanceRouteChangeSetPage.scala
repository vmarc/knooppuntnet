package kpn.api.common.longdistance

case class LongDistanceRouteChangeSetPage(
  id: Long,
  ref: Option[String],
  name: String,
  change: LongDistanceRouteChange
)
