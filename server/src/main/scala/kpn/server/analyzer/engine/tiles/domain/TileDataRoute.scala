package kpn.server.analyzer.engine.tiles.domain

import kpn.api.custom.Day

case class TileDataRoute(
  routeId: Long,
  routeName: String,
  layer: String,
  surveyDate: Option[Day],
  segments: Seq[TileRouteSegment]
)
