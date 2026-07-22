package kpn.server.monitor.domain

import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.custom.Timestamp
import kpn.api.id.WithObjectId
import org.bson.types.ObjectId

case class MonitorState(
  _id: ObjectId,
  routeId: ObjectId,
  relationId: Long,
  timestamp: Timestamp, // time of most recent analysis
  deviations: Seq[MonitorRouteDeviation],
  matchesDistance: Long,
  matchesLines: Seq[String],
  segments: Seq[MonitorSegment],
) extends WithObjectId
