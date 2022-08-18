package kpn.server.api.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId

case class MonitorRoute(
  _id: ObjectId,
  groupId: ObjectId,
  name: String,
  description: String,
  relationId: Option[Long]
) extends WithObjectId
