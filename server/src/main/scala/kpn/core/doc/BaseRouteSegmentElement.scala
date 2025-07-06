package kpn.core.doc

case class BaseRouteSegmentElement(
  segmentId: Long,
  segmentElementId: Long,
  surface: String,
  memberIndexes: Seq[Long],
  meters: Long,
  coordinates: String,
)
