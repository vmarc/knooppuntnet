package kpn.server.analyzer.engine.tiles.domain

import kpn.server.analyzer.engine.tile.ZoomLevel

object RouteTiles extends Tiles(
  (ZoomLevel.minZoom to ZoomLevel.maxZoom).map { zoomLevel =>
    val tileContext = TileContext.route(zoomLevel)
    zoomLevel -> tileContext
  }.toMap
)
