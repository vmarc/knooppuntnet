package kpn.server.monitor.domain

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.custom.Timestamp
import kpn.api.id.WithObjectId
import org.bson.types.ObjectId

case class OldMonitorReference(
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
