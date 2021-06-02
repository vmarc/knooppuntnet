package kpn.api.common.monitor

import kpn.api.common.changes.details.ChangeKey

case class MonitorRouteChangeSummary(
  key: ChangeKey,
  groupName: String,
  routeName: Option[String],
  groupDescription: Option[String],
  comment: Option[String],
  wayCount: Long,
  waysAdded: Long,
  waysRemoved: Long,
  waysUpdated: Long,
  osmDistance: Long,
  routeSegmentCount: Long,
  newNokSegmentCount: Long,
  resolvedNokSegmentCount: Long,
  happy: Boolean,
  investigate: Boolean
)
