package kpn.server.monitor.domain

import kpn.core.doc.Storable
import org.bson.types.ObjectId

case class MonitorGroupRouteCount(
  groupId: ObjectId,
  routeCount: Long
) extends Storable
