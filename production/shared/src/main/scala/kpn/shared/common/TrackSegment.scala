package kpn.shared.common

case class TrackSegment(
  surface: String,
  source: TrackPoint,
  fragments: Seq[TrackSegmentFragment]
)
