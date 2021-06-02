package kpn.api.common.monitor

import kpn.api.common.BoundsI
import kpn.api.common.changes.details.ChangeKey

case class LongdistanceRouteChangeSummary(
  key: ChangeKey,
  comment: Option[String],
  wayCount: Long,
  waysAdded: Long,
  waysRemoved: Long,
  waysUpdated: Long,
  osmDistance: Long,
  gpxDistance: Long,
  gpxFilename: String,
  bounds: BoundsI,
  routeSegmentCount: Long,
  newNokSegmentCount: Long,
  resolvedNokSegmentCount: Long,
  happy: Boolean,
  investigate: Boolean
)
