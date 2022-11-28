package kpn.server.repository

import kpn.api.base.ObjectId
import kpn.api.custom.Timestamp

case class MonitorRouteStateInfo(
  _id: ObjectId,
  timestamp: Timestamp,
)
