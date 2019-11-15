package kpn.api.common.common

case class TrackPath(
  startNodeId: Long,
  endNodeId: Long,
  meters: Long,
  segments: Seq[TrackSegment]
) {
  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("startNodeId", startNodeId).
    field("endNodeId", endNodeId).
    field("meters", meters).
    field("segments", segments).
    build
}
