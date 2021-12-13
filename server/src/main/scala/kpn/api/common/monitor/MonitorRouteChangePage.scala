package kpn.api.common.monitor

import kpn.api.common.Bounds
import kpn.api.common.changes.details.ChangeKey

case class MonitorRouteChangePage(
  key: ChangeKey,
  groupName: String,
  groupDescription: String,
  comment: Option[String],
  wayCount: Long,
  waysAdded: Long,
  waysRemoved: Long,
  waysUpdated: Long,
  osmDistance: Long,
  bounds: Bounds,
  routeSegmentCount: Long,
  routeSegments: Seq[MonitorRouteSegment],
  newNokSegments: Seq[MonitorRouteNokSegment],
  resolvedNokSegments: Seq[MonitorRouteNokSegment],
  reference: MonitorRouteReferenceInfo,
  happy: Boolean,
  investigate: Boolean
)
