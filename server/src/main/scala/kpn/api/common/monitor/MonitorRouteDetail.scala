package kpn.api.common.monitor

case class MonitorRouteDetail(
  monitorRouteId: String,
  routeId: Long,
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
