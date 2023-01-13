package kpn.server.api.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.Bounds
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class MonitorRouteReference(
  _id: ObjectId,
  routeId: ObjectId,
  relationId: Long, // 0 when gpx reference and osm relationId not known yet
  timestamp: Timestamp,
  user: String,
  bounds: Bounds,
  referenceType: String, // "osm" | "gpx"
  referenceDay: Day,
  distance: Long,
  segmentCount: Long,
  filename: Option[String],
  geoJson: String
) extends WithObjectId
