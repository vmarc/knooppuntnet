package kpn.core.tiles.domain

import kpn.api.common.LatLon

case class TileDataNode(
  id: Long,
  name: String,
  latitude: String,
  longitude: String,
  layer: String
) extends LatLon