package kpn.api.common.monitor

import kpn.api.common.BoundsI
import kpn.api.common.changes.details.ChangeKey

case class LongdistanceRouteChange(
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
  referenceJson: String,
  routeSegmentCount: Long,
  routeSegments: Seq[LongdistanceRouteSegment],
  newNokSegments: Seq[LongdistanceRouteNokSegment],
  resolvedNokSegments: Seq[LongdistanceRouteNokSegment],
  happy: Boolean,
  investigate: Boolean
)
