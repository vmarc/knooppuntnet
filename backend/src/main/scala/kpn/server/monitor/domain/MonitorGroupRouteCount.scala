package kpn.server.monitor.domain

import kpn.api.id.Storable
import org.bson.types.ObjectId

case class MonitorGroupRouteCount(
  groupId: ObjectId,
  routeCount: Long
) extends Storable
