package kpn.api.common.monitor

case class MonitorRouteDetailsPage(
  monitorRouteId: String,
  routeId: Long,
  routeName: String,
  groupName: String,
  groupDescription: String,
  description: String,
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long,
  gpxFilename: Option[String],
  happy: Boolean,
  osmSegmentCount: Long,
  gpxNokSegmentCount: Long
)
