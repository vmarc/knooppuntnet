package kpn.api.common.longdistance

import kpn.api.common.BoundsI
import kpn.api.common.changes.details.ChangeKeyI

case class LongDistanceRouteChangeSummary(
  key: ChangeKeyI,
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
