package kpn.server.monitor.repository

import kpn.api.base.ObjectId

case class MonitorStateId(
  _id: ObjectId,
  relationId: Long,
)
