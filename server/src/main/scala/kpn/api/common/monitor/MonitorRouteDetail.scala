package kpn.api.common.monitor

case class MonitorRouteDetail(
  monitorRouteId: String,
  routeId: Long,
  name: String,
  description: String,
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long,
  gpxFilename: Option[String],
  osmHappy: Boolean,
  gpxHappy: Boolean
)
