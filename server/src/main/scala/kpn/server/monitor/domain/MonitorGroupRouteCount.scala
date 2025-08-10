package kpn.server.monitor.domain

import org.bson.types.ObjectId

case class MonitorGroupRouteCount(groupId: ObjectId, routeCount: Long)
