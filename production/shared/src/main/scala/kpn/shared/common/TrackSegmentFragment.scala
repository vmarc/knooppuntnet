package kpn.shared.common

case class TrackSegmentFragment(
  trackPoint: TrackPoint,
  meters: Int,
  orientation: Double,
  streetIndex: Option[Int]
)
