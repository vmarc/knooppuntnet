package kpn.api.common.monitor

import kpn.api.common.BoundsI
import kpn.api.common.changes.details.ChangeKeyI

case class MonitorRouteChangeSummary(
  key: ChangeKeyI,
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
