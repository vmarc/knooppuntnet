package kpn.server.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.Bounds
import kpn.api.custom.Timestamp

case class MonitorRouteReference(
  _id: ObjectId,
  routeId: ObjectId,
  relationId: Option[Long], // None when route with referenceType "gpx" and osm relationId not known yet
  timestamp: Timestamp,
  user: String,
  bounds: Bounds,
  referenceType: String, // "osm" | "gpx" (subrelation references for "multi-gpx" routes also have reference type"gpx")
  referenceTimestamp: Timestamp,
  distance: Long,
  segmentCount: Long,
  filename: Option[String],
  geoJson: String
) extends WithObjectId
