package kpn.api.common.longdistance

import kpn.api.common.Bounds
import kpn.api.common.BoundsI

case class LongDistanceRouteMapPage(
  id: Long,
  ref: Option[String],
  name: String,
  nameNl: Option[String],
  nameEn: Option[String],
  nameDe: Option[String],
  nameFr: Option[String],
  bounds: BoundsI,
  gpxFilename: Option[String],
  osmSegments: Seq[LongDistanceRouteSegment],
  gpxGeometry: Option[String],
  okGeometry: Option[String],
  nokSegments: Seq[LongDistanceRouteNokSegment]
)
