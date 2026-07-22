package kpn.core.doc

import kpn.api.id.Storable

case class BaseRouteSegmentElement(
  segmentId: Long,
  segmentElementId: Long,
  surface: String,
  memberIndexes: Seq[Long],
  meters: Long,
  coordinates: String,
) extends Storable
