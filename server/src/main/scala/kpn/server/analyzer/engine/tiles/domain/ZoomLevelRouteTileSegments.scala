package kpn.server.analyzer.engine.tiles.domain

case class ZoomLevelRouteTileSegments(
  zoomLevel: Int,
  segments: Seq[RouteTileSegment]
)
