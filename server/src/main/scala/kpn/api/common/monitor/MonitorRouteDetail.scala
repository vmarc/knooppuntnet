package kpn.api.common.monitor

case class MonitorRouteDetail(
  index: Long,
  monitorRouteId: String,
  routeId: Long,
  name: String,
  description: String,
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long,
  gpxFilename: Option[String],
  osmHappy: Boolean,
  gpxHappy: Boolean,
  happy: Boolean,
)
