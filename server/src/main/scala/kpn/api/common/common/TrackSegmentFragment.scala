package kpn.api.common.common

case class TrackSegmentFragment(
  trackPoint: TrackPoint,
  meters: Long,
  orientation: Long,
  streetIndex: Option[Long]
) {
  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("trackPoint", trackPoint).
    field("meters", meters).
    field("orientation", orientation).
    field("streetIndex", streetIndex).
    build
}
