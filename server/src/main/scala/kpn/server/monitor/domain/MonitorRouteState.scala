package kpn.server.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.custom.Timestamp

case class MonitorRouteState(
  _id: ObjectId,
  routeId: ObjectId,
  relationId: Long,
  timestamp: Timestamp, // time of most recent analysis
  matchesDistance: Long,
  matchesGeometry: Option[String],
  deviations: Seq[MonitorRouteDeviation],
) extends WithObjectId
