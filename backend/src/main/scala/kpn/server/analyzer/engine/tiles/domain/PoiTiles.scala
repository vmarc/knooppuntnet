package kpn.server.analyzer.engine.tiles.domain

import kpn.server.analyzer.engine.tile.ZoomLevel

object PoiTiles extends Tiles(
  (ZoomLevel.poiTileMinZoom to ZoomLevel.poiTileMaxZoom).map { zoomLevel =>
    val tileContext = TileContext.poi(zoomLevel)
    zoomLevel -> tileContext
  }.toMap
)
