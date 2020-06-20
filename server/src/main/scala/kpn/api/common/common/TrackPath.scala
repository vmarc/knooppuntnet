package kpn.api.common.common

case class TrackPath(
  pathId: Long,
  startNodeId: Long,
  endNodeId: Long,
  meters: Long,
  oneWay: Boolean,
  segments: Seq[TrackSegment]
) {
  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("pathId", pathId).
    field("startNodeId", startNodeId).
    field("endNodeId", endNodeId).
    field("meters", meters).
    field("oneWay", oneWay).
    field("segments", segments).
    build
}
