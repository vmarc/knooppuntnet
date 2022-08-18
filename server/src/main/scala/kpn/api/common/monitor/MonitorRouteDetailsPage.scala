package kpn.api.common.monitor

case class MonitorRouteDetailsPage(
  routeId: String,
  groupName: String,
  groupDescription: String,
  routeName: String,
  routeDescription: String,
  relationId: Option[Long],
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long,
  gpxFilename: Option[String],
  happy: Boolean,
  osmSegmentCount: Long,
  gpxNokSegmentCount: Long
)
