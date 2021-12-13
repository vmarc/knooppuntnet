package kpn.api.common.monitor

import kpn.api.common.Bounds
import kpn.api.common.changes.details.ChangeKey

case class MonitorRouteChangeDetail(
  key: ChangeKey,
  comment: Option[String],
  wayCount: Long,
  waysAdded: Long,
  waysRemoved: Long,
  waysUpdated: Long,
  osmDistance: Long,
  gpxDistance: Long,
  gpxFilename: String,
  bounds: Bounds,
  referenceJson: String,
  routeSegmentCount: Long,
  routeSegments: Seq[MonitorRouteSegment],
  newNokSegments: Seq[MonitorRouteNokSegment],
  resolvedNokSegments: Seq[MonitorRouteNokSegment],
  happy: Boolean,
  investigate: Boolean
)
