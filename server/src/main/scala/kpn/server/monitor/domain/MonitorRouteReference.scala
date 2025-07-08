package kpn.server.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.custom.Timestamp

case class MonitorRouteReference(
  _id: ObjectId,
  routeId: ObjectId,
  relationId: Option[Long], // None when route with referenceType "gpx" and osm relationId not known yet
  timestamp: Timestamp,
  user: String,
  referenceBounds: Bounds,
  referenceType: MonitorReferenceType,
  referenceTimestamp: Timestamp,
  referenceDistance: Long,
  referenceSegmentCount: Long,
  referenceFilename: Option[String],
  referenceLines: Seq[String],
  referenceGeoJson: Option[String] = None,
) extends WithObjectId
