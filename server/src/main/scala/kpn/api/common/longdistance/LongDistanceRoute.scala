package kpn.api.common.longdistance

import kpn.api.common.Bounds

case class LongDistanceRoute(
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
  bounds: Bounds,
  gpxFilename: Option[String],
  osmSegments: Seq[LongDistanceRouteSegment],
  gpxGeometry: Option[String],
  okGeometry: Option[String],
  nokGeometry: Option[String]
)
