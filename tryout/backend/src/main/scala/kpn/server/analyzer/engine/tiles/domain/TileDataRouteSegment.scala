package kpn.server.analyzer.engine.tiles.domain

case class TileDataRouteSegment(
  segmentId: Long,
  segmentElementId: Long,
  pathIds: Seq[Long],
  oneWay: Boolean,
  surface: String,
  worldCoordinates: Seq[Double]
)
