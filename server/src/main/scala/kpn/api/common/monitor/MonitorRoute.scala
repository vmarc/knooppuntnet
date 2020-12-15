package kpn.api.common.monitor

import kpn.api.common.BoundsI

case class MonitorRoute(
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
  bounds: BoundsI,
  gpxFilename: Option[String],
  osmSegments: Seq[MonitorRouteSegment],
  gpxGeometry: Option[String],
  okGeometry: Option[String],
  nokSegments: Seq[MonitorRouteNokSegment]
)
