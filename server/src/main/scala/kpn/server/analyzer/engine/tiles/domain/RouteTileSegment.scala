package kpn.server.analyzer.engine.tiles.domain

case class RouteTileSegment(
  pathId: Long,
  oneWay: Boolean,
  surface: String,
  lines: Seq[Line]
)
