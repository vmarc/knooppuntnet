package kpn.api.common.monitor

import kpn.api.common.BoundsI
import kpn.api.common.changes.details.ChangeKeyI

case class MonitorRouteChangePage(
  key: ChangeKeyI,
  groupName: String,
  groupDescription: String,
  comment: Option[String],
  wayCount: Long,
  waysAdded: Long,
  waysRemoved: Long,
  waysUpdated: Long,
  osmDistance: Long,
  bounds: BoundsI,
  routeSegmentCount: Long,
  routeSegments: Seq[MonitorRouteSegment],
  newNokSegments: Seq[MonitorRouteNokSegment],
  resolvedNokSegments: Seq[MonitorRouteNokSegment],
  reference: MonitorRouteReferenceInfo,
  happy: Boolean,
  investigate: Boolean
)
