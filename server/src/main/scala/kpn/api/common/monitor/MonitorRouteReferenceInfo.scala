package kpn.api.common.monitor

import kpn.api.common.Bounds
import kpn.api.custom.Timestamp

case class MonitorRouteReferenceInfo(
  key: String,
  created: Timestamp,
  user: String,
  bounds: Bounds,
  distance: Long,
  referenceType: String, // "osm" | "gpx"
  referenceTimestamp: Option[Timestamp],
  segmentCount: Long,
  filename: Option[String],
  geometry: String
)
