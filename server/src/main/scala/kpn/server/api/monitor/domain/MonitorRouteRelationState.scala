package kpn.server.api.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteSegment

case class MonitorRouteRelationState(
  _id: ObjectId,
  routeId: ObjectId, // combination of routeId
  relationId: Long,  //   and relationId should be unique
  wayCount: Long,
  osmDistance: Long,
  bounds: Bounds,
  referenceId: Option[ObjectId], // reference to MonitorRouteReferenceRelation document
  osmSegments: Seq[MonitorRouteSegment],
  matchesGeometry: Option[String],
  deviations: Seq[MonitorRouteDeviation],
  happy: Boolean
) extends WithObjectId
