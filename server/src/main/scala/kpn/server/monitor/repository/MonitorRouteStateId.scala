package kpn.server.monitor.repository

import kpn.api.base.ObjectId

case class MonitorRouteStateId(
  _id: ObjectId,
  relationId: Long,
)
