package kpn.api.common.monitor

import kpn.api.common.Bounds
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class MonitorRouteReferenceInfo(
  created: Timestamp,
  user: String,
  bounds: Bounds,
  distance: Long,
  referenceType: String, // "osm" | "gpx"
  referenceDay: Option[Day],
  segmentCount: Long,
  gpxFilename: Option[String],
  geometry: String
)
