package kpn.shared.common

case class TrackSegmentFragment(
  trackPoint: TrackPoint,
  meters: Int,
  orientation: Int,
  streetIndex: Option[Int]
)
