package kpn.server.analyzer.engine.tiles.domain

import kpn.api.common.FeatureLayer
import kpn.api.custom.Day

case class TileDataRoute(
  routeId: Long,
  routeName: String,
  scopes: Seq[String],
  layer: FeatureLayer,
  surveyDate: Option[Day],
  state: Option[String],
  segments: Seq[TileDataRouteSegment],
  tiles: Seq[String]
)
