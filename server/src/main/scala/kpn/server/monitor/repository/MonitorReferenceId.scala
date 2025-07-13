package kpn.server.monitor.repository

import kpn.api.base.ObjectId

case class MonitorReferenceId(
  _id: ObjectId,
  relationId: Option[Long],
)
