package kpn.server.api.monitor.domain

import kpn.api.common.changes.details.ChangeKeyI
import kpn.api.common.monitor.MonitorRouteReferenceInfo

case class MonitorRouteChange(
  key: ChangeKeyI,
  groupName: String,
  wayCount: Long,
  waysAdded: Long,
  waysRemoved: Long,
  waysUpdated: Long,
  osmDistance: Long,
  routeSegmentCount: Long,
  reference: Option[MonitorRouteReferenceInfo],
  happy: Boolean,
  investigate: Boolean
)
