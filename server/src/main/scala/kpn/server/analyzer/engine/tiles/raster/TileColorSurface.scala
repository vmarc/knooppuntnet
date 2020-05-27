package kpn.server.analyzer.engine.tiles.raster

import java.awt.Color

import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.TileRouteSegment

class TileColorSurface extends TileColor {

  override def routeColor(route: TileDataRoute, segment: TileRouteSegment): Color = {
    if ("unpaved" == segment.surface) {
      TileColor.orange;
    }
    else {
      TileColor.green
    }
  }

  override def nodeColor(node: TileDataNode): Color = {
    TileColor.green
  }
}
