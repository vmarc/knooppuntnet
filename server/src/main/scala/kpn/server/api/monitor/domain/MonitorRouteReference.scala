package kpn.server.api.monitor.domain

import kpn.api.base.WithStringId
import kpn.api.common.Bounds
import kpn.api.custom.Timestamp

case class MonitorRouteReference(
  _id: String,
  monitorRouteId: String,
  routeId: Long,
  key: String, // YYYYMMDDHHMMSS derived from created Timestamp
  created: Timestamp,
  user: String,
  bounds: Bounds,
  referenceType: String, // "osm" | "gpx"
  referenceTimestamp: Option[Timestamp],
  segmentCount: Long,
  filename: Option[String],
  geometry: String // osm | gpx
) extends WithStringId
