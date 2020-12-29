package kpn.server.analyzer.engine.tiles.domain

import kpn.api.common.LatLon
import kpn.api.custom.Day

case class TileDataNode(
  id: Long,
  ref: Option[String],
  name: Option[String],
  latitude: String,
  longitude: String,
  layer: String,
  surveyDate: Option[Day]
) extends LatLon
