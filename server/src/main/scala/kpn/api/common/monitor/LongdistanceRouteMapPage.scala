package kpn.api.common.monitor

import kpn.api.common.BoundsI

case class LongdistanceRouteMapPage(
  id: Long,
  ref: Option[String],
  name: String,
  nameNl: Option[String],
  nameEn: Option[String],
  nameDe: Option[String],
  nameFr: Option[String],
  bounds: BoundsI,
  gpxFilename: Option[String],
  osmSegments: Seq[LongdistanceRouteSegment],
  gpxGeometry: Option[String],
  okGeometry: Option[String],
  nokSegments: Seq[LongdistanceRouteNokSegment]
)
