package kpn.api.common.monitor

import kpn.api.common.Bounds
import kpn.api.custom.Timestamp

case class MonitorRouteReferenceInfo(
  created: Timestamp,
  user: String,
  referenceBounds: Bounds,
  referenceDistance: Long,
  referenceType: String, // "osm" | "gpx" | "multi-gpx"
  referenceTimestamp: Timestamp,
  referenceSegmentCount: Long,
  referenceFilename: Option[String],
  referenceGeoJson: String
)
