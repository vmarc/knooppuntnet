package kpn.api.common.monitor

case class MonitorRouteDetail(
  id: Long,
  ref: Option[String],
  name: String,
  description: Option[String],
  operator: Option[String],
  website: Option[String],
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long,
  gpxFilename: Option[String],
  osmHappy: Boolean,
  gpxHappy: Boolean
)
