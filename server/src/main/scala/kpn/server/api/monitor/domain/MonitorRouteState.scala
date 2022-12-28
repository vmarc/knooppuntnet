package kpn.server.api.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.custom.Timestamp

case class MonitorRouteState(
  _id: ObjectId,
  routeId: ObjectId,
  relationId: Long,
  timestamp: Timestamp, // time of most recent analysis
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long, // TODO remove from this class (should be on reference only)
  bounds: Bounds,
  referenceId: Option[ObjectId],
  osmSegments: Seq[MonitorRouteSegment],
  matchesGeometry: Option[String],
  deviations: Seq[MonitorRouteDeviation],
  happy: Boolean,
  osmSuperSegments: Seq[MonitorRouteSuperSegment] // TODO move to MonitorRoute document
) extends WithObjectId
