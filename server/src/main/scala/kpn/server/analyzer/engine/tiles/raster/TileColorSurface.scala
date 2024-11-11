package kpn.server.analyzer.engine.tiles.raster

import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.TileDataRouteSegment

import java.awt.Color

class TileColorSurface extends TileColor {

  override def routeColor(route: TileDataRoute, segment: TileDataRouteSegment): Color = {
    if ("unpaved" == segment.surface) {
      TileColor.green
    }
    else if ("unknown" == segment.surface) {
      TileColor.orange
    }
    else {
      TileColor.blue
    }
  }

  override def nodeColor(node: TileDataNode): Color = {
    TileColor.blue
  }
}
