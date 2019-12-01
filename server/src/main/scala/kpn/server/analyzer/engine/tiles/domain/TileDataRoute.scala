package kpn.server.analyzer.engine.tiles.domain

case class TileDataRoute(
  routeId: Long,
  routeName: String,
  layer: String,
  surveyDate: Option[String],
  segments: Seq[TileRouteSegment]
)
