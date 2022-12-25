package kpn.server.api.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.Bounds

case class MonitorRouteRelationReference(
  _id: ObjectId,
  routeId: ObjectId,
  relationId: Long, // osm id of sub relation
  distance: Long,
  bounds: Bounds,
  segmentCount: Long,
  geometry: String
) extends WithObjectId
