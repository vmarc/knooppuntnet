package kpn.api.common.common

case class TrackSegment(
  surface: String,
  source: TrackPoint,
  fragments: Seq[TrackSegmentFragment]
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("surface", surface).
    field("source", source).
    field("fragments", fragments).
    build

}
