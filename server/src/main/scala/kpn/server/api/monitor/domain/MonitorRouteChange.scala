package kpn.server.api.monitor.domain

import kpn.api.common.changes.details.ChangeKey

case class MonitorRouteChange(
  key: ChangeKey,
  groupName: String,
  wayCount: Long,
  waysAdded: Long,
  waysRemoved: Long,
  waysUpdated: Long,
  osmDistance: Long,
  routeSegmentCount: Long,
  newNokSegmentCount: Long,
  resolvedNokSegmentCount: Long,
  referenceKey: String,
  happy: Boolean,
  investigate: Boolean
)
