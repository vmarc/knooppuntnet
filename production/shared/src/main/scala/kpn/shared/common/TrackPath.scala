package kpn.shared.common

case class TrackPath(
  startNodeId: Long,
  endNodeId: Long,
  meters: Int,
  segments: Seq[TrackSegment]
)
