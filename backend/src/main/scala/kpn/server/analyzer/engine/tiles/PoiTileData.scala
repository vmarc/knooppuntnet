package kpn.server.analyzer.engine.tiles

import kpn.core.poi.PoiInfo
import kpn.server.analyzer.engine.tiles.domain.Tile

case class PoiTileData(
  tile: Tile,
  pois: Seq[PoiInfo]
)
