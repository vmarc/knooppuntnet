package kpn.core.tiles

import kpn.core.poi.PoiInfo
import kpn.core.tiles.domain.Tile

case class PoiTileData(
  tile: Tile,
  pois: Seq[PoiInfo]
)
