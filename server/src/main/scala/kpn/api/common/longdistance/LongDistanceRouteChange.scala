package kpn.api.common.longdistance

import kpn.api.common.BoundsI
import kpn.api.common.changes.details.ChangeKeyI

case class LongDistanceRouteChange(
  key: ChangeKeyI,
  wayCount: Long,
  waysAdded: Long,
  waysRemoved: Long,
  waysUpdated: Long,
  osmDistance: Long,
  gpxDistance: Long,
  gpxFilename: String,
  bounds: BoundsI,
  referenceJson: String,
  routeSegments: Seq[LongDistanceRouteSegment],
  newNokSegments: Seq[LongDistanceRouteNokSegment],
  resolvedNokSegments: Seq[LongDistanceRouteNokSegment],
  happy: Boolean,
  investigate: Boolean
)
