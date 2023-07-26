package kpn.server.monitor.repository

import kpn.api.base.ObjectId

case class MonitorRouteReferenceId(
  _id: ObjectId,
  relationId: Option[Long],
)
