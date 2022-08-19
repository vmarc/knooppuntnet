package kpn.api.common.monitor

case class MonitorRouteDetail(
  rowIndex: Long,
  routeId: String,
  name: String,
  description: String,
  relationId: Option[Long],
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long,
  gpxFilename: Option[String],
  osmHappy: Boolean,
  gpxHappy: Boolean,
  happy: Boolean,
)
