package kpn.server.api.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.Bounds

case class OldMonitorRouteReferenceRelation(
  _id: ObjectId,
  referenceId: ObjectId, // id of MonitorRouteReference document
  relationId: Long, // osm id of sub relation
  bounds: Bounds,
  segmentCount: Long,
  geometry: String
) extends WithObjectId
