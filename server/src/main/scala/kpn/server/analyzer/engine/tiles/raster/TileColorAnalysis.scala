package kpn.server.analyzer.engine.tiles.raster

import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.TileDataRouteSegment

import java.awt.Color

class TileColorAnalysis extends TileColor {

  override def routeColor(route: TileDataRoute, segment: TileDataRouteSegment): Color = {
    route.layer match {
      case "orphan-route" => TileColor.darkGreen
      case "incomplete-route" => TileColor.red
      case "error-route" => TileColor.red
      case "route" => TileColor.green
      case _ => TileColor.green
    }
  }

  override def nodeColor(node: TileDataNode): Color = {
    node.layer match {
      case "error-orphan-node" => TileColor.darkRed
      case "orphan-node" => TileColor.darkGreen
      case "error-node" => TileColor.red
      case "node" => TileColor.green
      case _ => TileColor.green
    }
  }
}
