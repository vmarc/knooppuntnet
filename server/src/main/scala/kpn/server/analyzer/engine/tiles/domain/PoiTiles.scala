package kpn.server.analyzer.engine.tiles.domain

import kpn.api.common.tiles.ZoomLevel

object PoiTiles extends Tiles(
  (ZoomLevel.poiTileMinZoom to ZoomLevel.poiTileMaxZoom).map { zoomLevel =>
    val tileContext = TileContext.poi(zoomLevel)
    zoomLevel -> tileContext
  }.toMap
)
