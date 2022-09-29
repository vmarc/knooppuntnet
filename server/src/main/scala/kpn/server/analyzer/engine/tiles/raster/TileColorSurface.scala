package kpn.server.analyzer.engine.tiles.raster

import kpn.server.analyzer.engine.tiles.domain.RouteTileSegment
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute

import java.awt.Color

class TileColorSurface extends TileColor {

  override def routeColor(route: TileDataRoute, segment: RouteTileSegment): Color = {
    if ("unpaved" == segment.surface) {
      TileColor.orange
    }
    else if ("unknown" == segment.surface) {
      TileColor.mediumBlue
    }
    else {
      TileColor.green
    }
  }

  override def nodeColor(node: TileDataNode): Color = {
    TileColor.green
  }
}
