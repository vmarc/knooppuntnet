package kpn.shared.common

case class TrackSegmentFragment(
  trackPoint: TrackPoint,
  meters: Int,
  orientation: Int,
  streetIndex: Option[Int]
) {
  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("trackPoint", trackPoint).
    field("meters", meters).
    field("orientation", orientation).
    field("streetIndex", streetIndex).
    build
}
