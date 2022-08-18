package kpn.api.common.monitor

import kpn.api.base.ObjectId

case class MonitorRouteAdd(
  groupId: ObjectId,
  name: String,
  description: String,
  relationId: Option[Long]
)
