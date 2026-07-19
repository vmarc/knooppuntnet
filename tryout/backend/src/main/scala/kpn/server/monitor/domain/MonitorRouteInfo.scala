package kpn.server.monitor.domain

import org.bson.types.ObjectId

case class MonitorRouteInfo(
  _id: ObjectId,
  groupName: String,
  routeName: String,
)
