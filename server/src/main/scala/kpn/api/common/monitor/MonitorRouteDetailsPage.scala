package kpn.api.common.monitor

case class MonitorRouteDetailsPage(
  monitorRouteId: String,
  routeId: Long,
  routeName: String,
  groupName: String,
  groupDescription: String,
  nameNl: Option[String],
  nameEn: Option[String],
  nameDe: Option[String],
  nameFr: Option[String],
  ref: Option[String],
  description: Option[String],
  operator: Option[String],
  website: Option[String],
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long,
  gpxFilename: Option[String],
  happy: Boolean,
  osmSegmentCount: Long,
  gpxNokSegmentCount: Long
)
