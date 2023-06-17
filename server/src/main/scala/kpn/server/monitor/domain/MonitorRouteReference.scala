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
  referenceBounds: Bounds,
  referenceType: String, // "osm" | "gpx" (subrelation references for "multi-gpx" routes also have reference type"gpx")
  referenceTimestamp: Timestamp,
  referenceDistance: Long,
  referenceSegmentCount: Long,
  referenceFilename: Option[String],
  referenceGeoJson: String
) extends WithObjectId
