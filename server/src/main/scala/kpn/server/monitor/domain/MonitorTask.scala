package kpn.server.monitor.domain

import kpn.api.base.WithObjectId
import org.bson.types.ObjectId

case class MonitorTask(
  _id: ObjectId,
  priority: Long,
  message: String
) extends WithObjectId
