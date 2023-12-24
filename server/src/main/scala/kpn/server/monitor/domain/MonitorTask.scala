package kpn.server.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId

case class MonitorTask(
  _id: ObjectId,
  priority: Long,
  message: String
) extends WithObjectId
