package kpn.api.common.longdistance

case class LongDistanceRouteDetail(
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
