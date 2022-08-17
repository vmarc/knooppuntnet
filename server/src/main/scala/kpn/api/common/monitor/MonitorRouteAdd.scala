package kpn.api.common.monitor

import kpn.api.base.MongoId

case class MonitorRouteAdd(
  groupId: MongoId,
  name: String,
  description: String,
  relationId: Long
)
