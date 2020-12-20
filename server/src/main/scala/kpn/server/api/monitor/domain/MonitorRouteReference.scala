package kpn.server.api.monitor.domain

import kpn.api.common.BoundsI
import kpn.api.custom.Timestamp

case class MonitorRouteReference(
  routeId: Long,
  key: String, // YYYYMMDDHHMMSS derived from created Timestamp
  created: Timestamp,
  user: String,
  bounds: BoundsI,
  referenceType: String, // "osm" | "gpx"
  referenceTimestamp: Option[Timestamp],
  segmentCount: Long,
  filename: Option[String],
  geometry: String   // osm | gpx
)
