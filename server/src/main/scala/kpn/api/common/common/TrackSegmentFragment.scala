package kpn.api.common.common

case class TrackSegmentFragment(
  trackPoint: TrackPoint,
  meters: Long,
  orientation: Long,
  streetIndex: Option[Long]
) {
}
