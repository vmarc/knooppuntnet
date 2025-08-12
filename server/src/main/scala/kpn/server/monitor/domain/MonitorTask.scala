package kpn.server.monitor.domain

import kpn.core.doc.WithObjectId
import org.bson.types.ObjectId

case class MonitorTask(
  _id: ObjectId,
  priority: Long,
  message: String
) extends WithObjectId
