package kpn.server.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId

case class MonitorStateTile(
  _id: ObjectId,
  routeId: ObjectId,
  relationId: Long,
  z: Long,
  x: Long,
  y: Long,
  deviations: Seq[MonitorStateTileDeviation],
  matchesLines: Seq[String],
) extends WithObjectId
