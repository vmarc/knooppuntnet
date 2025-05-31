package kpn.server.analyzer.engine.tiles.domain

import kpn.api.common.FeatureLayer
import kpn.api.common.LatLon
import kpn.api.custom.Day

case class TileDataNode(
  nodeId: Long,
  ref: Option[String],
  name: Option[String],
  latitude: String,
  longitude: String,
  layer: FeatureLayer,
  surveyDate: Option[Day],
  proposed: Boolean
) extends LatLon
