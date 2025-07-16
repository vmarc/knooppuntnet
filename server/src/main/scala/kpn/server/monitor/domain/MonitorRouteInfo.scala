package kpn.server.monitor.domain

import kpn.api.base.ObjectId

case class MonitorRouteInfo(
  _id: ObjectId,
  groupName: String,
  routeName: String,
)
