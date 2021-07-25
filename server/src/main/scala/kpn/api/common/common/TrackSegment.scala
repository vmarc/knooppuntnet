package kpn.api.common.common

case class TrackSegment(
  surface: String,
  source: TrackPoint,
  fragments: Seq[TrackSegmentFragment]
) {

  def trackPoints: Seq[TrackPoint] = source +: fragments.map(_.trackPoint)

}
