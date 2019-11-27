package kpn.core.tiles.domain

case class TileDataRoute(
  routeId: Long,
  routeName: String,
  layer: String,
  surveyDate: Option[String],
  segments: Seq[TileRouteSegment]
)
