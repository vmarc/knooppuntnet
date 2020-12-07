package kpn.api.common.longdistance

case class LongDistanceRouteDetailsPage(
  id: Long,
  ref: Option[String],
  name: String,
  nameNl: Option[String],
  nameEn: Option[String],
  nameDe: Option[String],
  nameFr: Option[String],
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
