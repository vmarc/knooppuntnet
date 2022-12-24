package kpn.server.api.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.Bounds
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class OldMonitorRouteReference(
  _id: ObjectId,
  routeId: ObjectId,
  relationId: Option[Long],
  created: Timestamp,
  user: String,
  bounds: Bounds,
  referenceType: String, // "osm" | "gpx"
  referenceDay: Option[Day],
  segmentCount: Long,
  filename: Option[String],
  geometry: String // osm | gpx
) extends WithObjectId
