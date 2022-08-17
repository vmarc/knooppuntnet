package kpn.server.api.monitor.domain

import kpn.api.base.MongoId
import kpn.api.base.WithMongoId
import kpn.api.common.Bounds
import kpn.api.custom.Timestamp

case class MonitorRouteReference(
  _id: MongoId,
  monitorRouteId: MongoId,
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
) extends WithMongoId
