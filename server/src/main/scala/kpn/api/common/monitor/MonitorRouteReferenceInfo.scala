package kpn.api.common.monitor

import kpn.api.common.Bounds
import kpn.api.custom.Timestamp

case class MonitorRouteReferenceInfo(
  created: Timestamp,
  user: String,
  bounds: Bounds,
  distance: Long,
  referenceType: String, // "osm" | "gpx" | "multi-gpx"
  referenceTimestamp: Timestamp,
  segmentCount: Long,
  gpxFilename: Option[String],
  geoJson: String
)
