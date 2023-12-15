package kpn.server.analyzer.engine.tiles

import kpn.core.poi.PoiInfo
import kpn.server.analyzer.engine.tiles.domain.OldTile

case class PoiTileData(
  tile: OldTile,
  pois: Seq[PoiInfo]
)
